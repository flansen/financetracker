package flhan.de.financemanager.main.expenseoverview

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import javax.inject.Inject

class OverviewViewModelFactory @Inject constructor(
        private val schedulerProvider: SchedulerProvider,
        private val fetchExpensesInteractor: FetchExpensesInteractor)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseOverviewViewModel::class.java)) {
            return ExpenseOverviewViewModel(
                    fetchExpensesInteractor,
                    schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}