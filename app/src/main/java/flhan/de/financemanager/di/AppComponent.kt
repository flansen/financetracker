package flhan.de.financemanager.di

import dagger.Component
import flhan.de.financemanager.App
import flhan.de.financemanager.di.createjoinhousehold.CreateJoinHouseholdComponent
import flhan.de.financemanager.di.createjoinhousehold.CreateJoinHouseholdModule
import flhan.de.financemanager.di.main.expenseoverview.ExpenseOverviewComponent
import flhan.de.financemanager.di.main.expenseoverview.ExpenseOverviewModule
import flhan.de.financemanager.di.launcher.LauncherComponent
import flhan.de.financemanager.di.launcher.LauncherModule
import flhan.de.financemanager.di.signin.LoginComponent
import flhan.de.financemanager.di.signin.LoginModule
import javax.inject.Singleton

/**
 * Created by Florian on 10.09.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: App)
    fun plus(loginModule: LoginModule): LoginComponent
    fun plus(createJoinHouseholdModule: CreateJoinHouseholdModule): CreateJoinHouseholdComponent
    fun plus(expenseOverviewModule: ExpenseOverviewModule): ExpenseOverviewComponent
    fun plus(launcherModule: LauncherModule): LauncherComponent

}