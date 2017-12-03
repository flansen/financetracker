package flhan.de.financemanager.launcher

import dagger.Module
import dagger.Provides

/**
 * Created by fhansen on 06.10.17.
 */
@Module
class LauncherModule {

    @Provides
    fun presenter(checkAuthInteractor: CheckAuthInteractorImpl): LauncherContract.Presenter = LauncherPresenter(checkAuthInteractor)

}