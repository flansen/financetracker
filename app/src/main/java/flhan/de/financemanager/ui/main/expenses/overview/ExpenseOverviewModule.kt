package flhan.de.financemanager.ui.main.expenses.overview

import dagger.Module
import dagger.Provides

@Module
class ExpenseOverviewModule {

    @Provides
    fun fetchInteractor(interactor: ExpenseOverviewInteractorImpl): ExpenseOverviewInteractor = interactor

}