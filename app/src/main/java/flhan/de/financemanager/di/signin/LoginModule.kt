package flhan.de.financemanager.di.signin

import android.content.Context
import dagger.Module
import dagger.Provides
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.login.*

/**
 * Created by Florian on 10.09.2017.
 */
@Module class LoginModule(val activity: LoginActivity) {
    @Provides @ActivityScope
    fun loginView(): LoginContract.View = activity

    @Provides
    @ActivityScope
    fun activityContext(): Context = activity

    @Provides
    @ActivityScope
    fun providesPresenter(loginView: LoginContract.View,
                          loginInteractor: LoginInteractorImpl,
                          loginRouter: LoginRouterImpl,
                          schedulerProvider: SchedulerProvider): LoginContract.Presenter = LoginPresenter(loginView, loginInteractor, loginRouter, schedulerProvider)
}