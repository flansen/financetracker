package flhan.de.financemanager.di.overview

import dagger.Subcomponent
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.overview.OverviewActivity

/**
 * Created by Florian on 03.10.2017.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(OverviewModule::class))
interface OverviewComponent {
    fun inject(activity: OverviewActivity)
}