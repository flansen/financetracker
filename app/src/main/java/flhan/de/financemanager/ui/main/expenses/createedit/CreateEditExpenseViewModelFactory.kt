package flhan.de.financemanager.ui.main.expenses.createedit

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.datastore.UserSettings
import javax.inject.Inject

class CreateEditExpenseViewModelFactory @Inject constructor(
        private val findExpenseByIdInteractor: FindExpenseByIdInteractor,
        private val fetchUsersInteractor: FetchUsersInteractor,
        private val createUpdateExpenseInteractor: CreateUpdateExpenseInteractor,
        private val userSettings: UserSettings,
        private val schedulerProvider: SchedulerProvider,
        @CreateEditExpenseModule.ExpenseId private val id: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditExpenseViewModel::class.java)) {
            return CreateEditExpenseViewModel(createUpdateExpenseInteractor, schedulerProvider, findExpenseByIdInteractor, fetchUsersInteractor, userSettings, id) as T
        }
        throw IllegalArgumentException("${this.javaClass.name} cannot create a ViewModel of Type ${modelClass.name}")
    }
}