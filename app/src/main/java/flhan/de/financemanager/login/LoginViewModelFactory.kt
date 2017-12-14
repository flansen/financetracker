package flhan.de.financemanager.login

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.common.validators.NameValidator
import flhan.de.financemanager.login.createjoinhousehold.CreateHouseholdInteractor
import flhan.de.financemanager.login.createjoinhousehold.CreateJoinHouseholdViewModel
import flhan.de.financemanager.login.createjoinhousehold.JoinHouseholdByMailInteractor
import javax.inject.Inject

class LoginViewModelFactory @Inject constructor(
        private val loginInteractor: LoginInteractor,
        private val createHouseholdInteractor: CreateHouseholdInteractor,
        private val joinHouseholdByMailInteractor: JoinHouseholdByMailInteractor,
        private val schedulerProvider: SchedulerProvider,
        private val nameValidator: NameValidator,
        private val emailValidator: EmailValidator
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                    loginInteractor,
                    schedulerProvider) as T
        } else if (modelClass.isAssignableFrom(CreateJoinHouseholdViewModel::class.java)) {
            return CreateJoinHouseholdViewModel(nameValidator, emailValidator, createHouseholdInteractor, joinHouseholdByMailInteractor, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}