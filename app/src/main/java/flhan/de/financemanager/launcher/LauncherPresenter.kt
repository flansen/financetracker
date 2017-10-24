package flhan.de.financemanager.launcher

import flhan.de.financemanager.base.BasePresenter
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import io.reactivex.Observable

/**
 * Created by fhansen on 06.10.17.
 */
class LauncherPresenter(
        val interactor: CheckAuthInteractor,
        schedulerProvider: SchedulerProvider
) : BasePresenter(schedulerProvider), LauncherContract.Presenter {
    override lateinit var shouldPresentLogin: Observable<Boolean>

    override fun attach() {
        shouldPresentLogin = interactor.execute()
                .map { it.result == LauncherState.NotInitialized }
                .share()
    }

}