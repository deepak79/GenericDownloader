package com.mv.genericdownloader.ui.main


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mv.genericdownloader.BR
import com.mv.genericdownloader.R
import com.mv.genericdownloader.ViewModelProviderFactory
import com.mv.genericdownloader.databinding.ActivityMainBinding
import com.mv.genericdownloader.ui.base.BaseActivity
import com.mv.genericdownloader.ui.main.fragment.pinwall.PinWallFragment
import com.mv.genericdownloader.utils.FragmentInflater
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    MainNavigator,
    HasSupportFragmentInjector,
    View.OnClickListener {

    override val viewModel: MainViewModel
        get() = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private var binding: ActivityMainBinding? = null
    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        viewModel.setNavigator(this)
        setSupportActionBar(toolbar)
        setListeners()
        FragmentInflater.instance.inflate(
            PinWallFragment.newInstance(),
            this, R.id.container, PinWallFragment.TAG
        )
    }

    private fun setListeners() {
    }

    override fun onHandleError(error: String?) {
        showMessage(error)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentDispatchingAndroidInjector
    }

    override fun onClick(v: View?) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear_cache -> {
                (getCurrentFragment() as PinWallFragment).clearCache()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
