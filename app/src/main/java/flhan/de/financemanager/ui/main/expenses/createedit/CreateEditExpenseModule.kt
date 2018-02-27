package flhan.de.financemanager.ui.main.expenses.createedit

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.di.ExpenseId

@Module
class CreateEditExpenseModule {

    @Provides
    fun fetchInteractor(interactor: FindExpenseByIdInteractorImpl): FindExpenseByIdInteractor = interactor

    @Provides
    @ExpenseId
    fun providesId(activity: CreateEditExpenseActivity): String? = activity.id

    @Provides
    fun fetchUsersInteractor(interactor: FetchUsersInteractorImpl): FetchUsersInteractor = interactor

    @Provides
    fun saveExpenseInteractor(interactor: CreateUpdateExpenseInteractorImpl): CreateUpdateExpenseInteractor = interactor

    @Provides
    fun deleteInteractor(interactor: DeleteExpenseInteractorImpl): DeleteExpenseInteractor = interactor

}

