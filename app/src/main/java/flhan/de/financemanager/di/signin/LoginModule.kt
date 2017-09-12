package flhan.de.financemanager.di.signin

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.signin.LoginActivity
import flhan.de.financemanager.signin.LoginContract
import flhan.de.financemanager.signin.LoginInteractorImpl
import flhan.de.financemanager.signin.LoginPresenter

/**
 * Created by Florian on 10.09.2017.
 */
@Module class LoginModule(val activity: LoginActivity) {
    @Provides @ActivityScope
    fun loginView(): LoginContract.View = activity

    @Provides
    @ActivityScope
    fun providesPresenter(loginView: LoginContract.View, loginInteractor: LoginInteractorImpl): LoginContract.Presenter = LoginPresenter(loginView, loginInteractor)
}