package flhan.de.financemanager.ui.login.createjoinhousehold.join

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.GENERIC_ERROR_KEY
import flhan.de.financemanager.common.INVALID_SECRET_KEY
import flhan.de.financemanager.common.NO_SUCH_HOUSEHOLD_KEY
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinErrorState
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class JoinHouseholdViewModel(
        private val emailValidator: EmailValidator,
        private val joinHouseholdByMailInteractor: JoinHouseholdByMailInteractor,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    var joinEnabled: LiveData<Boolean>
    val isLoading = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<CreateJoinErrorState>()
    val mail = MutableLiveData<String>()
    val secret = MutableLiveData<String>()

    private val disposables = CompositeDisposable()

    init {
        isLoading.value = false
        errorState.value = CreateJoinErrorState(None)
        secret.value = ""
        mail.value = ""
        joinEnabled = Transformations.map(mail, { email -> emailValidator.validate(email) })
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }

    fun submit(success: () -> Unit) {
        joinHousehold(success)
    }


    private fun joinHousehold(success: () -> Unit) {
        joinHouseholdByMailInteractor.execute(mail.value!!, secret.value!!)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { result ->
                    handleInteractorResult(result, success)
                }
                .addTo(disposables)
    }

    private fun handleInteractorResult(result: InteractorResult<Household>, success: () -> Unit) {
        when (result.status) {
            InteractorStatus.Loading -> isLoading.value = true
            InteractorStatus.Error -> {
                isLoading.value = false
                onInteractorError(result.exception)
            }
            InteractorStatus.Success -> {
                isLoading.value = false
                success()
            }
        }
    }

    private fun onInteractorError(exception: Throwable?) {
        when (exception) {
            is NoSuchHouseholdThrowable -> errorState.value = CreateJoinErrorState(NoSuchHousehold, NO_SUCH_HOUSEHOLD_KEY)
            is InvalidSecretThrowable -> errorState.value = CreateJoinErrorState(InvalidSecret, INVALID_SECRET_KEY)
            else -> errorState.value = CreateJoinErrorState(Unknown, GENERIC_ERROR_KEY)
        }
    }
}