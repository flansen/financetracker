package flhan.de.financemanager.ui.main.expenses.overview

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ExpenseOverviewFragmentBuilder {

    @ContributesAndroidInjector(modules = [ExpenseOverviewModule::class])
    abstract fun provideExpenseOverviewFragmentFactory(): ExpenseOverviewFragment

}