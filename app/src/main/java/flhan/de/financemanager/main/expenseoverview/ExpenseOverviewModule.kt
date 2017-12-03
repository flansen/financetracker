package flhan.de.financemanager.main.expenseoverview

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.base.scheduler.SchedulerProvider

@Module
class ExpenseOverviewModule {

    @Provides
    fun providesPresenter(fetchExpensesInteractor: FetchExpensesInteractorImpl,
                          fragment: ExpenseOverviewFragment,
                          schedulerProvider: SchedulerProvider): ExpenseOverviewContract.Presenter = ExpenseOverviewPresenter(fragment, fetchExpensesInteractor, schedulerProvider)
}