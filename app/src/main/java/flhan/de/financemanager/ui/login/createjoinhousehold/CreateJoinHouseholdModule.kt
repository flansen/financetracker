package flhan.de.financemanager.ui.login.createjoinhousehold

import dagger.Module
import dagger.Provides

/**
 * Created by Florian on 29.09.2017.
 */
@Module
class CreateJoinHouseholdModule {

    @Provides
    fun createInteractor(interactor: CreateHouseholdInteractorImpl): CreateHouseholdInteractor = interactor

    @Provides
    fun joinInteractor(interactor: JoinHouseholdByMailInteractorImpl): JoinHouseholdByMailInteractor = interactor
}