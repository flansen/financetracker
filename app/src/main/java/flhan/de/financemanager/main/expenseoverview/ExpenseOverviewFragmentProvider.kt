package flhan.de.financemanager.main.expenseoverview

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ExpenseOverviewFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(ExpenseOverviewModule::class))
    abstract fun provideExpenseOverviewFragmentFactory(): ExpenseOverviewFragment

}