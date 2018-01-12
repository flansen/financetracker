package flhan.de.financemanager.ui.main.expenses.overview

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class OverviewViewModelFactory @Inject constructor(
        private val fetchExpensesInteractor: FetchExpensesInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseOverviewViewModel::class.java)) {
            return ExpenseOverviewViewModel(fetchExpensesInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}