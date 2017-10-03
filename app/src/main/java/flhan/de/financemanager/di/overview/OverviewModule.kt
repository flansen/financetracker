package flhan.de.financemanager.di.overview

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.di.ActivityScope
import flhan.de.financemanager.overview.OverviewActivity
import flhan.de.financemanager.overview.OverviewContract
import flhan.de.financemanager.overview.OverviewPresenter

/**
 * Created by Florian on 03.10.2017.
 */
@Module
class OverviewModule(
        private val activity: OverviewActivity
) {
    @Provides
    @ActivityScope
    fun providesView(): OverviewActivity = activity

    @Provides
    @ActivityScope
    fun providesPresenter(): OverviewContract.Presenter = OverviewPresenter(activity)
}