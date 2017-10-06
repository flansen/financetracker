package flhan.de.financemanager.di.launcher

import dagger.Subcomponent
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.launcher.LauncherActivity

/**
 * Created by fhansen on 06.10.17.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(LauncherModule::class))
interface LauncherComponent {
    fun inject(activity: LauncherActivity)
}