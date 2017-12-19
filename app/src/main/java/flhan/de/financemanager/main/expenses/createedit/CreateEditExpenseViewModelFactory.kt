package flhan.de.financemanager.main.expenses.createedit

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class CreateEditExpenseViewModelFactory @Inject constructor(
        private val findExpenseByIdInteractor: FindExpenseByIdInteractor,
        @CreateEditExpenseModule.ExpenseId private val id: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditExpenseViewModel::class.java)) {
            return CreateEditExpenseViewModel(findExpenseByIdInteractor, id) as T
        }
        throw IllegalArgumentException("${this.javaClass.name} cannot create a ViewModel of Type ${modelClass.name}")
    }
}