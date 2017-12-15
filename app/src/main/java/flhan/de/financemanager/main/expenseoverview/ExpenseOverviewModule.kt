package flhan.de.financemanager.main.expenseoverview

import dagger.Module
import dagger.Provides

@Module
class ExpenseOverviewModule {

    @Provides
    fun fetchInteractor(interactor: FetchExpensesInteractorImpl): FetchExpensesInteractor = interactor

}