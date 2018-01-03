package flhan.de.financemanager.ui.login.createjoinhousehold.join

import dagger.Module
import dagger.Provides

/**
 * Created by Florian on 29.09.2017.
 */
@Module
class JoinHouseholdModule {

    @Provides
    fun joinInteractor(interactor: JoinHouseholdByMailInteractorImpl): JoinHouseholdByMailInteractor = interactor
}