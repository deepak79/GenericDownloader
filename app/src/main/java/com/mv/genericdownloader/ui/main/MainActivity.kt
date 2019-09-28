package com.mv.genericdownloader.ui.main


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mv.genericdownloader.BR
import com.mv.genericdownloader.R
import com.mv.genericdownloader.ViewModelProviderFactory
import com.mv.genericdownloader.databinding.ActivityMainBinding
import com.mv.genericdownloader.ui.base.BaseActivity
import com.mv.genericdownloader.ui.main.fragment.pinwall.PinWallFragment
import com.mv.genericdownloader.utils.FragmentInflater
import com.mv.genericdownloader.utils.IOnBackPressed
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
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
        setSupportActionBar(binding!!.toolbar)
        FragmentInflater.instance.inflate(
            PinWallFragment.newInstance(),
            this, R.id.container, PinWallFragment.TAG
        )
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
        return when (item.itemId) {
            R.id.menu_clear_cache -> {
                (getCurrentFragment() as PinWallFragment).clearCache()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 0 && supportFragmentManager.backStackEntryCount > 0) {
            if (getCurrentFragment() is PinWallFragment) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Exit Application")
                builder.setMessage("Do you really want to exit?")
                builder.setIcon(R.drawable.ic_exit)
                builder.setPositiveButton("Yes") { dialogInterface, which ->
                    dialogInterface.dismiss()
                    finish()
                }
                builder.setNegativeButton("No") { dialogInterface, which ->
                    dialogInterface.dismiss()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            } else {
                if (getCurrentFragment() !is IOnBackPressed || !(getCurrentFragment() as IOnBackPressed).onBackPressed()) {
                    super.onBackPressed()
                }
            }
        } else {
            finish()
        }
    }
}
