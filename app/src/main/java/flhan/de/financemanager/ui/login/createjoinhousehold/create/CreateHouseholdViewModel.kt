package flhan.de.financemanager.ui.login.createjoinhousehold.create

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.GENERIC_ERROR_KEY
import flhan.de.financemanager.common.NO_SUCH_HOUSEHOLD_KEY
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.common.validators.NameValidator
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinErrorState
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.*
import flhan.de.financemanager.ui.login.createjoinhousehold.InputState
import flhan.de.financemanager.ui.login.createjoinhousehold.join.NoSuchHouseholdThrowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class CreateHouseholdViewModel(
        private val nameValidator: NameValidator,
        private val createHouseholdInteractor: CreateHouseholdInteractor,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val createEnabled = MediatorLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<CreateJoinErrorState>()
    val name = MutableLiveData<String>()
    val secret = MutableLiveData<String>()

    private val inputStateMediator: MediatorLiveData<InputState>
    private val disposables = CompositeDisposable()

    init {
        createEnabled.value = false
        isLoading.value = false
        errorState.value = CreateJoinErrorState(None)
        secret.value = ""
        name.value = ""

        inputStateMediator = MediatorLiveData()
        inputStateMediator.addSource(name, { Unit })
        inputStateMediator.addSource(secret, { Unit })

        createEnabled.addSource(inputStateMediator, { inputState ->
            createEnabled.value = nameValidator.validate(name.value ?: "") && secret.value!!.length > 3
        })
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }

    fun submit(success: () -> Unit) {
        createHousehold(success)
    }

    private fun createHousehold(success: () -> Unit) {
        createHouseholdInteractor.execute(name.value!!)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { result ->
                    handleInteractorResult(result, success)
                }
                .addTo(disposables)
    }

    private fun handleInteractorResult(result: InteractorResult<Household>, success: () -> Unit) {
        when (result.status) {
            Loading -> isLoading.value = true
            Error -> {
                isLoading.value = false
                onInteractorError(result.exception)
            }
            Success -> {
                isLoading.value = false
                success()
            }
        }
    }

    private fun onInteractorError(exception: Throwable?) {
        when (exception) {
            is NoSuchHouseholdThrowable -> errorState.value = CreateJoinErrorState(NoSuchHousehold, NO_SUCH_HOUSEHOLD_KEY)
            else -> errorState.value = CreateJoinErrorState(Unknown, GENERIC_ERROR_KEY)
        }
    }
}