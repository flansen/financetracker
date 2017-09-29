package flhan.de.financemanager.di.createjoinhousehold

import dagger.Subcomponent
import flhan.de.financemanager.createjoinhousehold.CreateJoinHouseholdActivity
import flhan.de.financemanager.di.ActivityScope

/**
 * Created by Florian on 29.09.2017.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(CreateJoinHouseholdModule::class))
interface CreateJoinHouseholdComponent {
    fun inject(activity: CreateJoinHouseholdActivity)
}