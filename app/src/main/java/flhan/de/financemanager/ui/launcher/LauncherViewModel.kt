package flhan.de.financemanager.ui.launcher

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.ui.launcher.LauncherState.NotInitialized
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class LauncherViewModel(
        interactor: CheckAuthInteractor,
        schedulerProvider: SchedulerProvider
) : ViewModel() {

    val showLogin = MutableLiveData<Boolean>()

    private val disposables = CompositeDisposable()

    init {
        interactor.execute()
                .map { it.result == NotInitialized }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { showLogin.value = it }
                .addTo(disposables)
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }
}