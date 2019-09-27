package com.mv.genericdownloader.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mv.genericdownloader.BR
import com.mv.genericdownloader.R
import com.mv.genericdownloader.ViewModelProviderFactory
import com.mv.genericdownloader.databinding.ActivityDetailBinding
import com.mv.genericdownloader.ui.base.BaseActivity
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import com.mv.genericdownloaderlib.enums.ResourceTypes
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>(),
    DetailNavigator,
    HasSupportFragmentInjector {

    override val viewModel: DetailViewModel
        get() = ViewModelProviders.of(this, factory).get(DetailViewModel::class.java)
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private var binding: ActivityDetailBinding? = null
    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_detail

    lateinit var mResourceURL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        viewModel.setNavigator(this)
        binding!!.btnBack.setOnClickListener {
            finish()
        }
        if (intent != null && intent.extras != null
            && intent!!.extras!!.containsKey("URL")
        ) {
            mResourceURL = intent?.extras?.getString("URL")!!

            GenericDownloadManager(
                mResourceURL,
                ResourceTypes.IMAGE, object : IResourceRequestCallBack<BaseResource> {
                    override fun onSuccess(data: BaseResource) {
                        binding!!.imgDetail.setImageBitmap((data as ImageResource).getBitmap())
                    }

                    override fun onFailure(error: String?) {
                        Log.e("@@@@", "Failure $error")
                    }
                })
        }
    }

    override fun onHandleError(error: String?) {
        showMessage(error)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentDispatchingAndroidInjector
    }
}