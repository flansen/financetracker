package flhan.de.financemanager.di.main.expenseoverview

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewContract
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewFragment
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewPresenter
import flhan.de.financemanager.main.expenseoverview.FetchExpensesInteractorImpl

/**
 * Created by Florian on 03.10.2017.
 */
@Module
class ExpenseOverviewModule(
        private val activityExpense: ExpenseOverviewFragment
) {
    @Provides
    @ActivityScope
    fun providesView(): ExpenseOverviewFragment = activityExpense

    @Provides
    @ActivityScope
    fun providesPresenter(fetchExpensesInteractor: FetchExpensesInteractorImpl, schedulerProvider: SchedulerProvider): ExpenseOverviewContract.Presenter
            = ExpenseOverviewPresenter(activityExpense, fetchExpensesInteractor, schedulerProvider)
}