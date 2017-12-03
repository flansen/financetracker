package flhan.de.financemanager.login

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.base.scheduler.SchedulerProvider

/**
 * Created by Florian on 10.09.2017.
 */
@Module
class LoginModule {

    @Provides
    fun providesPresenter(loginView: LoginActivity,
                          loginInteractor: LoginInteractorImpl,
                          loginRouter: LoginRouterImpl,
                          schedulerProvider: SchedulerProvider): LoginContract.Presenter = LoginPresenter(loginView, loginInteractor, loginRouter, schedulerProvider)
}