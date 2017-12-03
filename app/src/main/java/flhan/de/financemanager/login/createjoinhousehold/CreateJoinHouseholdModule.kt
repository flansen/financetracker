package flhan.de.financemanager.login.createjoinhousehold

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.common.validators.NameValidator

/**
 * Created by Florian on 29.09.2017.
 */
@Module
class CreateJoinHouseholdModule {

    @Provides
    fun providesPresenter(view: CreateJoinHouseholdActivity,
                          emailValidator: EmailValidator,
                          nameValidator: NameValidator,
                          createHouseholdInteractor: CreateHouseholdInteractorImpl,
                          joinHouseholdByMailInteractor: JoinHouseholdByMailInteractorImpl): CreateJoinHouseholdContract.Presenter = CreateJoinHouseholdPresenter(view, nameValidator, emailValidator, createHouseholdInteractor, joinHouseholdByMailInteractor)
}