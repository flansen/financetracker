package flhan.de.financemanager.main.expenses.overview

import dagger.Module
import dagger.Provides

@Module
class ExpenseOverviewModule {

    @Provides
    fun fetchInteractor(interactor: FetchExpensesInteractorImpl): FetchExpensesInteractor = interactor

}