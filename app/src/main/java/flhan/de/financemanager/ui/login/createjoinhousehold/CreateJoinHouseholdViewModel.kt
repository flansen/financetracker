package flhan.de.financemanager.ui.login.createjoinhousehold

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.NO_SUCH_HOUSEHOLD_KEY
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.common.validators.NameValidator
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinHouseholdViewModel.CreateJoinFocusTarget.Email
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinHouseholdViewModel.CreateJoinFocusTarget.Name
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.NoSuchHousehold
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.Unknown
import flhan.de.financemanager.ui.login.createjoinhousehold.InputState.Create
import flhan.de.financemanager.ui.login.createjoinhousehold.InputState.Join

class CreateJoinHouseholdViewModel(
        private val nameValidator: NameValidator,
        private val emailValidator: EmailValidator,
        private val createHouseholdInteractor: CreateHouseholdInteractor,
        private val joinHouseholdByMailInteractor: JoinHouseholdByMailInteractor,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val canSubmit = MediatorLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<CreateJoinErrorState>()
    val name = MutableLiveData<String>()
    val mail = MutableLiveData<String>()

    private val inputStateMediator: MediatorLiveData<InputState>

    init {
        canSubmit.value = false
        isLoading.value = false
        errorState.value = CreateJoinErrorState(ErrorType.None)

        inputStateMediator = MediatorLiveData()
        inputStateMediator.addSource(name, { inputStateMediator.value = Create })
        inputStateMediator.addSource(mail, { inputStateMediator.value = Join })

        canSubmit.addSource(inputStateMediator, { inputState ->
            var isValid = false
            when (inputState) {
                Create -> isValid = nameValidator.validate(name.value ?: "")
                Join -> isValid = emailValidator.validate(mail.value ?: "")
            }
            canSubmit.value = isValid
        })
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
            null -> TODO()
        }
    }

    private fun createHousehold(success: () -> Unit) {
        createHouseholdInteractor.execute(name.value!!)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { interactorResult ->
                    handleInteractorResult(interactorResult, success)
                }
    }

    private fun joinHousehold(success: () -> Unit) {
        joinHouseholdByMailInteractor.execute(mail.value!!)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { interactorResult ->
                    handleInteractorResult(interactorResult, success)
                }
    }

    private fun handleInteractorResult(interactorResult: InteractorResult<Household>, success: () -> Unit) {
        when (interactorResult.status) {
            Loading -> isLoading.value = true
            Error -> {
                isLoading.value = false
                onInteractorError(interactorResult.exception)
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
            else -> errorState.value = CreateJoinErrorState(Unknown, NO_SUCH_HOUSEHOLD_KEY)
        }
    }

    enum class CreateJoinFocusTarget {
        Name, Email
    }
}
