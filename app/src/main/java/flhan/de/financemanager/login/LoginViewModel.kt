package flhan.de.financemanager.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.scheduler.SchedulerProvider

class LoginViewModel(private val loginInteractor: LoginInteractor,
                     private val router: LoginRouter,
                     private val schedulerProvider: SchedulerProvider) : ViewModel() {

    val error = MutableLiveData<String>()
    val loginSuccess = MutableLiveData<Boolean>()

    fun startAuth(token: String) {
        loginInteractor.login(token)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { loginResult ->
                    loginSuccess.value = loginResult.isSuccessful
                    if (loginResult.isSuccessful) {
                        router.navigateToCreateJoinHousehold()
                    } else {
                        error.value = loginResult.error
                    }
                }
    }
}