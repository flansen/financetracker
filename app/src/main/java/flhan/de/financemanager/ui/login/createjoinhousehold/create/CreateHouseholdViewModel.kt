package flhan.de.financemanager.ui.login.createjoinhousehold.create

import android.arch.lifecycle.*
import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.GENERIC_ERROR_KEY
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.common.validators.LengthValidator
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinErrorState
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.None
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.Unknown
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class CreateHouseholdViewModel(
        private val lengthValidator: LengthValidator,
        private val createHouseholdInteractor: CreateHouseholdInteractor,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val createEnabled: LiveData<Boolean>
    val isLoading = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<CreateJoinErrorState>()
    val name = MutableLiveData<String>()
    val secret = MutableLiveData<String>()

    private val validationTrigger: MediatorLiveData<Unit>
    private val disposables = CompositeDisposable()

    init {
        isLoading.value = false
        errorState.value = CreateJoinErrorState(None)
        secret.value = ""
        name.value = ""

        validationTrigger = MediatorLiveData()
        validationTrigger.addSource(name, { validationTrigger.value = Unit })
        validationTrigger.addSource(secret, { validationTrigger.value = Unit })

        createEnabled = Transformations.map(validationTrigger, { _ ->
            lengthValidator.validate(name.value) && lengthValidator.validate(secret.value)
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
        val householdName = name.value?.let { it } ?: return
        val householdSecret = secret.value?.let { it } ?: return
        createHouseholdInteractor.execute(householdName, householdSecret)
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
                onInteractorError()
            }
            Success -> {
                isLoading.value = false
                success()
            }
        }
    }

    private fun onInteractorError() {
        errorState.value = CreateJoinErrorState(Unknown, GENERIC_ERROR_KEY)
    }
}