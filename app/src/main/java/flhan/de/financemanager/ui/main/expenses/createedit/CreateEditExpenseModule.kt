package flhan.de.financemanager.ui.main.expenses.createedit

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME


@Module
class CreateEditExpenseModule {

    @Provides
    fun fetchInteractor(interactor: FindExpenseByIdInteractorImpl): FindExpenseByIdInteractor = interactor

    @Provides
    @ExpenseId
    fun providesId(activity: CreateEditExpenseActivity): String? = activity.retrieveExpenseId()

    @Provides
    fun fetchUsersInteractor(interactor: FetchUsersInteractorImpl): FetchUsersInteractor = interactor


    @Qualifier
    @MustBeDocumented
    @Retention(RUNTIME)
    annotation class ExpenseId
}

