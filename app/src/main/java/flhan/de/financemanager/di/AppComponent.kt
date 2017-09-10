package flhan.de.financemanager.di

import dagger.Component
import flhan.de.financemanager.App
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
}