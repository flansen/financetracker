package flhan.de.financemanager.ui.login.createjoinhousehold.create

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.validators.LengthValidator
import javax.inject.Inject

class CreateHouseholdViewModelFactory @Inject constructor(
        private val createHouseholdInteractor: CreateHouseholdInteractor,
        private val schedulerProvider: SchedulerProvider,
        private val lengthValidator: LengthValidator
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateHouseholdViewModel::class.java)) {
            return CreateHouseholdViewModel(lengthValidator, createHouseholdInteractor, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}