package flhan.de.financemanager.ui.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.extensions.cleanUp
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class LoginViewModel(
        private val loginInteractor: LoginInteractor,
        private val schedulerProvider: SchedulerProvider)
    : ViewModel() {

    val error = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun startAuth(token: String, success: () -> Unit) {
        loginInteractor.login(token)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { loginResult ->
                    if (loginResult.isSuccessful) {
                        success()
                    } else {
                        error.value = loginResult.error
                    }
                }
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.cleanUp()
        super.onCleared()
    }
}