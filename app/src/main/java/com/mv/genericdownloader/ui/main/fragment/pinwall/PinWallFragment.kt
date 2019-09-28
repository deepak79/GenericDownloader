package com.mv.genericdownloader.ui.main.fragment.pinwall

import android.annotation.SuppressLint
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
import com.mv.genericdownloader.utils.IOnBackPressed
import com.mv.genericdownloader.utils.PaginationListener
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import javax.inject.Inject


class PinWallFragment : BaseFragment<FragmentPinwallBinding, PinWallVM>(),
    PinWallNavigator,
    SwipeRefreshLayout.OnRefreshListener,
IOnBackPressed{

    companion object {
        val TAG = PinWallFragment::class.java.simpleName
        fun newInstance(): PinWallFragment {
            val args = Bundle()
            val fragment = PinWallFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var factory: ViewModelProviderFactory
    private var isDataLoading = false
    var viewModels: PinWallVM? = null
    var binding: FragmentPinwallBinding? = null
    lateinit var mPinWallAdapter: PinWallAdapter

    override val bindingVariable :Int
        get() =  BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_pinwall

    override val viewModel: PinWallVM
        get() = getPinWallVM()

    fun getPinWallVM():PinWallVM{
        viewModels = ViewModelProviders.of(this, factory).get(PinWallVM::class.java)
        return viewModels!!
    }


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModels!!.setNavigator(this)
    }
    /**
     * To clear the cache from memory and request new data
     * */
    fun clearCache() {
        if (mPinWallAdapter != null) {
            GenericDownloadManager.clearCache()
            mPinWallAdapter.clear()
            requestGetData()
        }
    }

    override fun onViewCreated(@NonNull view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        binding!!.executePendingBindings()
        binding!!.swipeRefresh.setOnRefreshListener(this)
        setRecyclerview()
        binding!!.fabGotoTop.setOnClickListener {
            binding!!.fabGotoTop.hide()
            binding!!.rvImagesWall.smoothScrollToPosition(0)
        }
        observeDataResponse()
        requestGetData()
    }


    /**
     * To set recyclerview
     * */
    fun setRecyclerview() {
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (mPinWallAdapter != null) {
                    return when (mPinWallAdapter.getItemViewType(position)) {
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
        mPinWallAdapter = PinWallAdapter(mutableListOf())
        binding!!.rvImagesWall.setHasFixedSize(true)
        binding!!.rvImagesWall.layoutManager = layoutManager
        binding!!.rvImagesWall.addOnScrollListener(object : PaginationListener(layoutManager,binding!!.fabGotoTop) {
            override val isLoading: Boolean
                get() = isDataLoading

            override fun loadMoreItems() {
                requestGetData()
            }
        })
        binding!!.rvImagesWall.adapter = mPinWallAdapter
    }


    /**
     * To show error to the user
     * */
    override fun onHandleError(error: String?) {
        showMessage(error)
    }

    /**
     * To request data from network
     * */
    private fun requestGetData() {
        if (isNetworkConnected) {
            binding!!.tvMesssage.visibility = GONE
            binding!!.rvImagesWall.visibility = VISIBLE
            mPinWallAdapter.addLoading()
            isDataLoading = true
            viewModel.getDataFromRemote()
        } else {
            binding!!.tvMesssage.visibility = VISIBLE
            binding!!.rvImagesWall.visibility = GONE
        }
    }

    /**
     * To observe changes made in data response live data
     * */
    private fun observeDataResponse() {
        viewModel.getDataResponseLiveData().observe(this, Observer {
            mPinWallAdapter.addItems(it)
            isDataLoading = false
            binding!!.swipeRefresh.isRefreshing = false
        })
    }

    /**
     * SwipeRefresh method
     * */
    override fun onRefresh() {
        mPinWallAdapter.clear()
        viewModel.reset()
        requestGetData()
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}