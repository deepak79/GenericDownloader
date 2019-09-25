package com.mv.genericdownloader.ui.main.fragment.pinwall

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mv.genericdownloader.BR
import com.mv.genericdownloader.R
import com.mv.genericdownloader.ViewModelProviderFactory
import com.mv.genericdownloader.databinding.FragmentPinwallBinding
import com.mv.genericdownloader.ui.base.BaseFragment
import com.mv.genericdownloader.ui.main.fragment.pinwall.adapter.MenuImagesAdapter
import com.mv.genericdownloader.utils.GridItemDecoration
import com.mv.genericdownloader.utils.PaginationListener
import com.mv.genericdownloader.utils.PaginationListener.Companion.PAGE_START
import javax.inject.Inject


class PinWallFragment : BaseFragment<FragmentPinwallBinding,
        PinWallVM>(), PinWallNavigator {

    private var currentPage = PAGE_START
    private val isTheLastPage = false
    private val totalPage = 10
    private var isDataLoading = false
    var itemCount = 0

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
    var viewModels: PinWallVM? = null
    var binding: FragmentPinwallBinding? = null
    lateinit var adapter: MenuImagesAdapter
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModels!!.setNavigator(this)
    }

    override fun onViewCreated(@NonNull view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        binding!!.executePendingBindings()
        binding!!.rvImagesWall.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding!!.rvImagesWall.layoutManager = layoutManager
        binding!!.rvImagesWall.addItemDecoration(GridItemDecoration(10, 2))
        binding!!.rvImagesWall.addOnScrollListener(object : PaginationListener(layoutManager) {
            override val isLastPage: Boolean
                get() = isTheLastPage
            override val isLoading: Boolean
                get() = isDataLoading

            override fun loadMoreItems() {
                isDataLoading = true
                currentPage++
                requestGetData()
            }
        })
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
        viewModel.getData()
    }

    private fun observeDataResponse() {
        viewModel.getDataResponseLiveData().observe(this, Observer {
            adapter = MenuImagesAdapter(
                context!!,
                it
            )
            binding!!.rvImagesWall.adapter = adapter
        })
    }
}