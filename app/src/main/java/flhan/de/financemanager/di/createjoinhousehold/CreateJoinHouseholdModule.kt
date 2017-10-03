package flhan.de.financemanager.di.createjoinhousehold

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.common.validators.NameValidator
import flhan.de.financemanager.createjoinhousehold.*
import flhan.de.financemanager.di.ActivityScope

/**
 * Created by Florian on 29.09.2017.
 */
@Module
class CreateJoinHouseholdModule(
        private val activity: CreateJoinHouseholdActivity
) {
    @Provides
    @ActivityScope
    fun createJoinView(): CreateJoinHouseholdContract.View = activity

    @Provides
    @ActivityScope
    fun providesPresenter(view: CreateJoinHouseholdContract.View,
                          emailValidator: EmailValidator,
                          nameValidator: NameValidator,
                          createHouseholdInteractor: CreateHouseholdInteractorImpl,
                          joinHouseholdInteractor: JoinHouseholdInteractorImpl,
                          joinHouseholdByMailInteractor: JoinHouseholdByMailInteractorImpl): CreateJoinHouseholdContract.Presenter = CreateJoinHouseholdPresenter(view, nameValidator, emailValidator, createHouseholdInteractor, joinHouseholdInteractor, joinHouseholdByMailInteractor)
}