package flhan.de.financemanager.launcher

import android.content.Intent
import android.os.Bundle
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.app
import flhan.de.financemanager.di.launcher.LauncherModule
import flhan.de.financemanager.login.LoginActivity
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewFragment
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LauncherActivity : BaseActivity(), LauncherContract.View {
    private val component by lazy { app.appComponent.plus(LauncherModule(this)) }

    @Inject
    lateinit var presenter: LauncherContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        presenter.attach()

        presenter.shouldPresentLogin.subscribe { shouldPresentLogin ->
            if (shouldPresentLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, ExpenseOverviewFragment::class.java))
            }
        }.addTo(disposables)
    }
}
