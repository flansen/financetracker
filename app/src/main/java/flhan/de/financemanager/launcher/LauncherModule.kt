package flhan.de.financemanager.launcher

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.base.scheduler.SchedulerProvider

/**
 * Created by fhansen on 06.10.17.
 */
@Module
class LauncherModule {

    @Provides
    fun presenter(checkAuthInteractor: CheckAuthInteractorImpl,
                  schedulerProvider: SchedulerProvider): LauncherContract.Presenter = LauncherPresenter(checkAuthInteractor, schedulerProvider)

}