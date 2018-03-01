package flhan.de.financemanager.ui.main.expenses.createedit

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.datastore.UserExpenseDataStore
import io.reactivex.Observable
import javax.inject.Inject

interface CreateUpdateExpenseInteractor {
    fun save(expense: Expense): Observable<InteractorResult<Unit>>
}

class CreateUpdateExpenseInteractorImpl @Inject constructor(private val userExpenseDataStore: UserExpenseDataStore
) : CreateUpdateExpenseInteractor {

    override fun save(expense: Expense): Observable<InteractorResult<Unit>> {
        return userExpenseDataStore
                .saveExpense(expense)
                .map { InteractorResult(Success, it, null) }
                .startWith(InteractorResult(Loading))
                .onErrorResumeNext { error: Throwable -> Observable.just(InteractorResult<Unit>(Error, null, error)) }
    }
}