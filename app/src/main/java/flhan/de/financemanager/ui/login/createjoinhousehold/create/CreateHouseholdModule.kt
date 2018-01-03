package flhan.de.financemanager.ui.login.createjoinhousehold.create

import dagger.Module
import dagger.Provides

/**
 * Created by Florian on 29.09.2017.
 */
@Module
class CreateHouseholdModule {

    @Provides
    fun createInteractor(interactor: CreateHouseholdInteractorImpl): CreateHouseholdInteractor = interactor
}