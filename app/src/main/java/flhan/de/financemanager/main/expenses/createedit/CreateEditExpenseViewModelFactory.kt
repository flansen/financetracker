package flhan.de.financemanager.main.expenses.createedit

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class CreateEditExpenseViewModelFactory @Inject constructor() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditExpenseViewModel::class.java)) {
            return CreateEditExpenseViewModel() as T
        }
        throw IllegalArgumentException("${this.javaClass.name} cannot create a ViewModel of Type ${modelClass.name}")
    }
}