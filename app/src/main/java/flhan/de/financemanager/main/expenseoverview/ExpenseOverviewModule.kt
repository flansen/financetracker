package flhan.de.financemanager.main.expenseoverview

import dagger.Module
import dagger.Provides

@Module
class ExpenseOverviewModule {

    @Provides
    fun providesPresenter(fetchExpensesInteractor: FetchExpensesInteractorImpl,
                          fragment: ExpenseOverviewFragment): ExpenseOverviewContract.Presenter = ExpenseOverviewPresenter(fragment, fetchExpensesInteractor)
}