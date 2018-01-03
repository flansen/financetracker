package flhan.de.financemanager.ui.login.createjoinhousehold.join

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.validators.EmailValidator
import javax.inject.Inject

class JoinHouseholdViewModelFactory @Inject constructor(
        private val joinHouseholdByMailInteractor: JoinHouseholdByMailInteractor,
        private val schedulerProvider: SchedulerProvider,
        private val emailValidator: EmailValidator
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JoinHouseholdViewModel::class.java)) {
            return JoinHouseholdViewModel(emailValidator, joinHouseholdByMailInteractor, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}