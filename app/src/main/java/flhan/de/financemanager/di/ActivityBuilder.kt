package flhan.de.financemanager.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import flhan.de.financemanager.ui.launcher.LauncherActivity
import flhan.de.financemanager.ui.launcher.LauncherModule
import flhan.de.financemanager.ui.login.LoginActivity
import flhan.de.financemanager.ui.login.LoginModule
import flhan.de.financemanager.ui.login.createjoinhousehold.create.CreateHouseholdActivity
import flhan.de.financemanager.ui.login.createjoinhousehold.create.CreateHouseholdModule
import flhan.de.financemanager.ui.login.createjoinhousehold.join.JoinHouseholdActivity
import flhan.de.financemanager.ui.login.createjoinhousehold.join.JoinHouseholdModule
import flhan.de.financemanager.ui.main.MainActivity
import flhan.de.financemanager.ui.main.MainActivityModule
import flhan.de.financemanager.ui.main.expenses.createedit.CreateEditExpenseActivity
import flhan.de.financemanager.ui.main.expenses.createedit.CreateEditExpenseModule
import flhan.de.financemanager.ui.main.expenses.overview.ExpenseOverviewFragmentBuilder

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [LauncherModule::class])
    abstract fun bindLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector(modules = [CreateHouseholdModule::class])
    abstract fun bindCreateHouseholdActivity(): CreateHouseholdActivity

    @ContributesAndroidInjector(modules = [JoinHouseholdModule::class])
    abstract fun bindJoinHouseholdActivity(): JoinHouseholdActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class, ExpenseOverviewFragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [CreateEditExpenseModule::class])
    abstract fun bindCreateEditExpenseActivity(): CreateEditExpenseActivity
}