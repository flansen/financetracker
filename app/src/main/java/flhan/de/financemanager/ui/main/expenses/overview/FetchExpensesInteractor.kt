package flhan.de.financemanager.ui.main.expenses.overview

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.datastore.RemoteDataStore
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 07.10.2017.
 */
interface FetchExpensesInteractor {
    fun fetchAll(): Observable<InteractorResult<List<Expense>>>
}

class FetchExpensesInteractorImpl @Inject constructor(private val dataStore: RemoteDataStore
) : FetchExpensesInteractor {

    override fun fetchAll(): Observable<InteractorResult<List<Expense>>> {
        return dataStore
                // TODO: Handle error cases?
                .loadExpenses()
                .map { expenseList ->
                    InteractorResult(Success, expenseList.toList())
                }
                .replay(1)
                .refCount()
    }
}