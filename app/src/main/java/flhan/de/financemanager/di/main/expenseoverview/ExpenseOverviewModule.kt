package flhan.de.financemanager.di.main.expenseoverview

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewFragment
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewContract
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewPresenter

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
    fun providesPresenter(): ExpenseOverviewContract.Presenter = ExpenseOverviewPresenter(activityExpense)
}