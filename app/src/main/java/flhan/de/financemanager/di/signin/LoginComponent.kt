package flhan.de.financemanager.di.signin

import dagger.Subcomponent
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.signin.LoginActivity
import javax.inject.Singleton

/**
 * Created by Florian on 10.09.2017.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(LoginModule::class))
interface LoginComponent {
    fun inject(activity: LoginActivity)
}