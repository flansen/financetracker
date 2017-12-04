package flhan.de.financemanager.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import flhan.de.financemanager.launcher.LauncherActivity
import flhan.de.financemanager.launcher.LauncherModule
import flhan.de.financemanager.login.LoginActivity
import flhan.de.financemanager.login.LoginModule
import flhan.de.financemanager.login.createjoinhousehold.CreateJoinHouseholdActivity
import flhan.de.financemanager.login.createjoinhousehold.CreateJoinHouseholdModule
import flhan.de.financemanager.main.MainActivity
import flhan.de.financemanager.main.MainActivityModule
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewFragmentBuilder

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(LoginModule::class))
    abstract fun bindLoginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(LauncherModule::class))
    abstract fun bindLauncherActivity(): LauncherActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(CreateJoinHouseholdModule::class))
    abstract fun bindCreateJoinHouseholdActivity(): CreateJoinHouseholdActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class, ExpenseOverviewFragmentBuilder::class))
    abstract fun bindMainActivity(): MainActivity

}