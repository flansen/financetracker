package flhan.de.financemanager.di.launcher

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.launcher.CheckAuthInteractorImpl
import flhan.de.financemanager.launcher.LauncherContract
import flhan.de.financemanager.launcher.LauncherPresenter

/**
 * Created by fhansen on 06.10.17.
 */
@Module
class LauncherModule(
        val view: LauncherContract.View
) {
    @Provides
    @ActivityScope
    fun presenter(checkAuthInteractor: CheckAuthInteractorImpl): LauncherContract.Presenter = LauncherPresenter(checkAuthInteractor)
}