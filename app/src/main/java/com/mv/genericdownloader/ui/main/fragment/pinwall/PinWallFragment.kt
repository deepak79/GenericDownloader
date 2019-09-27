package com.mv.genericdownloader.ui.main.fragment.pinwall

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mv.genericdownloader.BR
import com.mv.genericdownloader.R
import com.mv.genericdownloader.ViewModelProviderFactory
import com.mv.genericdownloader.databinding.FragmentPinwallBinding
import com.mv.genericdownloader.ui.base.BaseFragment
import com.mv.genericdownloader.ui.main.fragment.pinwall.adapter.PinWallAdapter
import com.mv.genericdownloader.utils.PaginationListener
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import kotlinx.android.synthetic.main.fragment_pinwall.*
import javax.inject.Inject


class PinWallFragment : BaseFragment<FragmentPinwallBinding, PinWallVM>(),
    PinWallNavigator,
    SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val TAG = PinWallFragment::class.java.simpleName
        fun newInstance(): PinWallFragment {
            val args = Bundle()
            val fragment = PinWallFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var isDataLoading = false
    var itemCount = 0
    @Inject
    lateinit var factory: ViewModelProviderFactory
    var viewModels: PinWallVM? = null
    var binding: FragmentPinwallBinding? = null
    lateinit var adapter: PinWallAdapter
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModels!!.setNavigator(this)
    }

    fun clearCache() {
        if (adapter != null) {
            GenericDownloadManager.clearCache()
            itemCount = 0
            adapter.clear()
            requestGetData()
        }
    }

    override fun onViewCreated(@NonNull view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        binding!!.executePendingBindings()
        swipeRefresh.setOnRefreshListener(this)
        adapter = PinWallAdapter(mutableListOf())
        binding!!.rvImagesWall.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context, 2)
        binding!!.rvImagesWall.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter != null) {
                    return when (adapter.getItemViewType(position)) {
                        1 -> {
                            1
                        }
                        0 -> {
                            2
                        }
                        else -> -1
                    }
                } else {
                    return -1
                }
            }
        }
        binding!!.rvImagesWall.addOnScrollListener(object : PaginationListener(layoutManager) {
            override val isLoading: Boolean
                get() = isDataLoading

            override fun loadMoreItems() {
                requestGetData()
            }
        })
        binding!!.rvImagesWall.adapter = adapter
        binding!!.fabGotoTop.setOnClickListener {
            binding!!.fabGotoTop.visibility = GONE
            binding!!.rvImagesWall.smoothScrollToPosition(0)
        }
        observeDataResponse()
        requestGetData()
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pinwall
    }

    override fun getViewModel(): PinWallVM {
        viewModels = ViewModelProviders.of(this, factory).get(PinWallVM::class.java)
        return viewModels!!
    }

    override fun onHandleError(error: String?) {
        showMessage(error)
    }

    private fun requestGetData() {
        if (isNetworkConnected) {
            binding!!.tvMesssage.visibility = GONE
            binding!!.rvImagesWall.visibility = VISIBLE
            adapter.addLoading()
            isDataLoading = true
            viewModel.getDataFromRemote()
        } else {
            binding!!.tvMesssage.visibility = VISIBLE
            binding!!.rvImagesWall.visibility = GONE
        }
    }

    private fun observeDataResponse() {
        viewModel.getDataResponseLiveData().observe(this, Observer {
            adapter.addItems(it)
            isDataLoading = false
            swipeRefresh.isRefreshing = false
            if (adapter.mList.size > 10) {
                binding!!.fabGotoTop.visibility = VISIBLE
            }
        })
    }

    override fun onRefresh() {
        itemCount = 0
        adapter.clear()
        requestGetData()
    }
}