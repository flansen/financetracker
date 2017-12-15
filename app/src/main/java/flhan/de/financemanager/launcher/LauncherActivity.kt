package flhan.de.financemanager.launcher

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.login.LoginActivity
import flhan.de.financemanager.main.MainActivity
import javax.inject.Inject

class LauncherActivity : BaseActivity() {

    @Inject
    lateinit var factory: LauncherViewModelFactory

    private lateinit var viewModel: LauncherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(LauncherViewModel::class.java)

        viewModel.showLogin.observe(this, Observer { presentLogin ->
            presentLogin?.apply {
                if (presentLogin) {
                    startActivity(Intent(this@LauncherActivity, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
                }
            }
        })
    }
}
