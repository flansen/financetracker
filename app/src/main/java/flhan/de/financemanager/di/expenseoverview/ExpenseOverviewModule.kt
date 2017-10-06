package flhan.de.financemanager.di.expenseoverview

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.expenseoverview.ExpenseOverviewActivity
import flhan.de.financemanager.expenseoverview.ExpenseOverviewContract
import flhan.de.financemanager.expenseoverview.ExpenseOverviewPresenter

/**
 * Created by Florian on 03.10.2017.
 */
@Module
class ExpenseOverviewModule(
        private val activityExpense: ExpenseOverviewActivity
) {
    @Provides
    @ActivityScope
    fun providesView(): ExpenseOverviewActivity = activityExpense

    @Provides
    @ActivityScope
    fun providesPresenter(): ExpenseOverviewContract.Presenter = ExpenseOverviewPresenter(activityExpense)
}