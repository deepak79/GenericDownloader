package com.mv.genericdownloader.ui.main.fragment.pinwall

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mv.genericdownloader.BR
import com.mv.genericdownloader.R
import com.mv.genericdownloader.ViewModelProviderFactory
import com.mv.genericdownloader.databinding.FragmentPinwallBinding
import com.mv.genericdownloader.ui.base.BaseFragment
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource
import com.mv.genericdownloaderlib.model.ResourceTypes
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
        //viewModel.getData()
        val gem = GenericDownloadManager(
            "https://images.unsplash.com/profile-1464495186405-68089dcd96c3?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=faces\\u0026fit=crop\\u0026h=64\\u0026w=64\\u0026s=ef631d113179b3137f911a05fea56d23",
            ResourceTypes.IMAGE
            , object : IResourceRequestCallBack<BaseResource> {
                override fun onSuccess(data: BaseResource) {
                    img.setImageBitmap((data as ImageResource).getBitmap())
                }

                override fun onFailure(error: String?) {
                    Log.e("@@@@", "Failure $error")
                }
            })
    }

    private fun observeDataResponse() {
        viewModel.getDataResponseLiveData().observe(this, Observer {
            //            val list = ArrayList<Menuimage>()
//            val menuImage = Menuimage()
//            menuImage.image = "https://via.placeholder.com/600x400?text=Image+Not+Available"
//            list.add(menuImage)
//
//            adapter = MenuImagesAdapter(
//                this,
//                list
//            )
//            val linearLayoutManager =
//                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//
//            val snapHelper = PagerSnapHelper()
//            snapHelper.attachToRecyclerView(binding!!.rvImages)
//            binding!!.rvImages.addItemDecoration(CirclePagerIndicatorDecoration())
//
//            binding!!.rvImages.layoutManager = linearLayoutManager
//            binding!!.rvImages.adapter = adapter
        })
    }
}