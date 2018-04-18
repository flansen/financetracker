package flhan.de.financemanager.ui.main.expenses.createedit

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Loading
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.datastore.ExpenseDataStore
import io.reactivex.Observable
import javax.inject.Inject

interface FindExpenseByIdInteractor {
    fun findExpense(id: String): Observable<InteractorResult<Expense>>
}

class FindExpenseByIdInteractorImpl @Inject constructor(private val dataStore: ExpenseDataStore
) : FindExpenseByIdInteractor {

    override fun findExpense(id: String): Observable<InteractorResult<Expense>> {
        return dataStore.findExpenseBy(id)
                .filter { it.exception == null }
                .map { InteractorResult(Success, it.result!!) }
                .startWith(InteractorResult(Loading))
    }
}