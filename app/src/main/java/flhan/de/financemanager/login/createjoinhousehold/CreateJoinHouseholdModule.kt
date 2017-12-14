package flhan.de.financemanager.login.createjoinhousehold

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.login.LoginInteractor
import flhan.de.financemanager.login.LoginInteractorImpl

/**
 * Created by Florian on 29.09.2017.
 */
@Module
class CreateJoinHouseholdModule {

    @Provides
    fun createInteractor(interactor: CreateHouseholdInteractorImpl): CreateHouseholdInteractor = interactor

    @Provides
    fun joinInteractor(interactor: JoinHouseholdByMailInteractorImpl): JoinHouseholdByMailInteractor = interactor

    @Provides
    fun interactor(interactor: LoginInteractorImpl): LoginInteractor = interactor

}