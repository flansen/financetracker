package flhan.de.financemanager.login

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(
        private val loginInteractor: LoginInteractor,
        private val schedulerProvider: SchedulerProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                    loginInteractor,
                    schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}