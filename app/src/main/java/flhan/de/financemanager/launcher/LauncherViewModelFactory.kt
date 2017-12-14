package flhan.de.financemanager.launcher

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import javax.inject.Inject

class LauncherViewModelFactory @Inject constructor(
        private val interactor: CheckAuthInteractor,
        private val schedulerProvider: SchedulerProvider)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LauncherViewModel::class.java)) {
            return LauncherViewModel(
                    interactor,
                    schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}