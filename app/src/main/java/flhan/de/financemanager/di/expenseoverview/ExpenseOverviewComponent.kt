package flhan.de.financemanager.di.expenseoverview

import dagger.Subcomponent
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.expenseoverview.ExpenseOverviewActivity

/**
 * Created by Florian on 03.10.2017.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(ExpenseOverviewModule::class))
interface ExpenseOverviewComponent {
    fun inject(activityExpense: ExpenseOverviewActivity)
}