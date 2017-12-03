package flhan.de.financemanager.main.expenseoverview

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ExpenseOverviewFragmentBuilder {

    @ContributesAndroidInjector(modules = arrayOf(ExpenseOverviewModule::class))
    abstract fun provideExpenseOverviewFragmentFactory(): ExpenseOverviewFragment

}