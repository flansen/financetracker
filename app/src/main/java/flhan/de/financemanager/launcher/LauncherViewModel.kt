package flhan.de.financemanager.launcher

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.launcher.LauncherState.NotInitialized

class LauncherViewModel(
        interactor: CheckAuthInteractor,
        schedulerProvider: SchedulerProvider)
    : ViewModel() {

    val showLogin = MutableLiveData<Boolean>()

    init {
        interactor.execute()
                .map { it.result == NotInitialized }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { showLogin.value = it }

    }
}