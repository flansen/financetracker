package flhan.de.financemanager.ui.main.expenses.createedit

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.common.UserSettings
import javax.inject.Inject

class CreateEditExpenseViewModelFactory @Inject constructor(
        private val findExpenseByIdInteractor: FindExpenseByIdInteractor,
        private val fetchUsersInteractor: FetchUsersInteractor,
        private val userSettings: UserSettings,
        @CreateEditExpenseModule.ExpenseId private val id: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditExpenseViewModel::class.java)) {
            return CreateEditExpenseViewModel(findExpenseByIdInteractor, fetchUsersInteractor, userSettings, id) as T
        }
        throw IllegalArgumentException("${this.javaClass.name} cannot create a ViewModel of Type ${modelClass.name}")
    }
}