package flhan.de.financemanager.di.main.expenseoverview

import dagger.Subcomponent
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewFragment

/**
 * Created by Florian on 03.10.2017.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(ExpenseOverviewModule::class))
interface ExpenseOverviewComponent {
    fun inject(fragment: ExpenseOverviewFragment)
}