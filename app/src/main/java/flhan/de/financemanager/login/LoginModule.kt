package flhan.de.financemanager.login

import dagger.Module
import dagger.Provides

/**
 * Created by Florian on 10.09.2017.
 */
@Module
class LoginModule {

    @Provides
    fun providesPresenter(loginView: LoginActivity,
                          loginInteractor: LoginInteractorImpl,
                          loginRouter: LoginRouterImpl): LoginContract.Presenter = LoginPresenter(loginView, loginInteractor, loginRouter)
}