package com.mv.genericdownloader.ui.main.fragment.pinwall

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mv.genericdownloader.BR
import com.mv.genericdownloader.R
import com.mv.genericdownloader.ViewModelProviderFactory
import com.mv.genericdownloader.databinding.FragmentPinwallBinding
import com.mv.genericdownloader.ui.base.BaseFragment
import com.mv.genericdownloader.ui.main.fragment.pinwall.adapter.MenuImagesAdapter
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource
import com.mv.genericdownloaderlib.model.ResourceTypes
import com.mv.genericdownloaderlib.utils.SingletonHolder
import kotlinx.android.synthetic.main.fragment_pinwall.*
import javax.inject.Inject


class PinWallFragment : BaseFragment<FragmentPinwallBinding,
        PinWallVM>(), PinWallNavigator {
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
    lateinit var adapter:MenuImagesAdapter
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModels!!.setNavigator(this)
    }

    override fun onViewCreated(@NonNull view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        binding!!.executePendingBindings()
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
            binding!!.rvImagesWall.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding!!.rvImagesWall.adapter = adapter
        })
    }
}