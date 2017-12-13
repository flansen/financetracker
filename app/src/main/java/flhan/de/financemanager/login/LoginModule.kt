package flhan.de.financemanager.login

import android.content.Context
import dagger.Module
import dagger.Provides
import flhan.de.financemanager.base.scheduler.SchedulerProvider

/**
 * Created by Florian on 10.09.2017.
 */
@Module
class LoginModule {

    @Provides
    fun router(context: Context): LoginRouter = LoginRouterImpl(context)

    @Provides
    fun interactor(authManager: AuthManager): LoginInteractor = LoginInteractorImpl(authManager)
}