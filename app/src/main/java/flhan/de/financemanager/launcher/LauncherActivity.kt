package flhan.de.financemanager.launcher

import android.content.Intent
import android.os.Bundle
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.app
import flhan.de.financemanager.di.launcher.LauncherModule
import flhan.de.financemanager.login.LoginActivity
import flhan.de.financemanager.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LauncherActivity : BaseActivity(), LauncherContract.View {
    private val component by lazy { app.appComponent.plus(LauncherModule(this)) }

    @Inject
    lateinit var presenter: LauncherContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        presenter.attach()

        presenter.shouldPresentLogin
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { shouldPresentLogin ->
                    if (shouldPresentLogin) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }.addTo(disposables)
    }
}
