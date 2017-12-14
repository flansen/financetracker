package flhan.de.financemanager.login

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.login.createjoinhousehold.CreateHouseholdInteractor
import flhan.de.financemanager.login.createjoinhousehold.CreateHouseholdInteractorImpl
import flhan.de.financemanager.login.createjoinhousehold.JoinHouseholdByMailInteractor
import flhan.de.financemanager.login.createjoinhousehold.JoinHouseholdByMailInteractorImpl

/**
 * Created by Florian on 10.09.2017.
 */
@Module
class LoginModule {

    @Provides
    fun interactor(interactor: LoginInteractorImpl): LoginInteractor = interactor

    @Provides
    fun createInteractor(interactor: CreateHouseholdInteractorImpl): CreateHouseholdInteractor = interactor

    @Provides
    fun joinInteractor(interactor: JoinHouseholdByMailInteractorImpl): JoinHouseholdByMailInteractor = interactor

}