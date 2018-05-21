package flhan.de.financemanager.ui.launcher

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.start
import flhan.de.financemanager.ui.login.LoginActivity
import flhan.de.financemanager.ui.main.MainActivity
import javax.inject.Inject

class LauncherActivity : BaseActivity() {

    @Inject
    lateinit var factory: LauncherViewModelFactory

    private lateinit var viewModel: LauncherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(LauncherViewModel::class.java)

        viewModel.showLogin.observe(this, Observer { presentLogin ->
            presentLogin ?: return@Observer
            if (presentLogin) {
                start(LoginActivity::class)
            } else {
                start(MainActivity::class)
            }
            finish()
        })
    }
}
