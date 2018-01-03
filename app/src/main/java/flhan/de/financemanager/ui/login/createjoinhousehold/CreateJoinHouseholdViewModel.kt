package flhan.de.financemanager.ui.login.createjoinhousehold

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
import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.common.validators.NameValidator
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinFocusTarget.Email
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinFocusTarget.Name
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.NoSuchHousehold
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.Unknown
import flhan.de.financemanager.ui.login.createjoinhousehold.InputState.Create
import flhan.de.financemanager.ui.login.createjoinhousehold.InputState.Join
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class CreateJoinHouseholdViewModel(
        private val nameValidator: NameValidator,
        private val emailValidator: EmailValidator,
        private val createHouseholdInteractor: CreateHouseholdInteractor,
        private val joinHouseholdByMailInteractor: JoinHouseholdByMailInteractor,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val joinEnabled = MediatorLiveData<Boolean>()
    val createEnabled = MediatorLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<CreateJoinErrorState>()
    val name = MutableLiveData<String>()
    val mail = MutableLiveData<String>()

    private val inputStateMediator: MediatorLiveData<InputState>
    private val disposables = CompositeDisposable()

    init {
        joinEnabled.value = false
        createEnabled.value = false
        isLoading.value = false
        errorState.value = CreateJoinErrorState(ErrorType.None)

        inputStateMediator = MediatorLiveData()
        inputStateMediator.addSource(name, { inputStateMediator.value = Create })
        inputStateMediator.addSource(mail, { inputStateMediator.value = Join })

        joinEnabled.addSource(inputStateMediator, { inputState ->
            joinEnabled.value = inputState == Join && emailValidator.validate(mail.value ?: "")
        })

        createEnabled.addSource(inputStateMediator, { inputState ->
            createEnabled.value = inputState == Create && nameValidator.validate(name.value ?: "")
        })
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }

    fun focusChanged(target: CreateJoinFocusTarget) {
        when (target) {
            Name -> mail.value = ""
            Email -> name.value = ""
        }
    }

    fun submit(success: () -> Unit) {
        when (inputStateMediator.value) {
            Join -> joinHousehold(success)
            Create -> createHousehold(success)
        // FIXME: is this an error?
            null -> TODO()
        }
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

    private fun joinHousehold(success: () -> Unit) {
        joinHouseholdByMailInteractor.execute(mail.value!!)
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

enum class CreateJoinFocusTarget {
    Name, Email
}
