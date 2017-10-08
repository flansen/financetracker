package flhan.de.financemanager.main.expenseoverview

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.RemoteDataStore
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.events.RepositoryEvent
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 07.10.2017.
 */
interface FetchExpensesInteractor {
    fun fetchAll(): Observable<InteractorResult<RepositoryEvent<Expense>>>
}

class FetchExpensesInteractorImpl @Inject constructor(
        val dataStore: RemoteDataStore
) : FetchExpensesInteractor {

    override fun fetchAll(): Observable<InteractorResult<RepositoryEvent<Expense>>> {
        return dataStore.loadExpenses().map { event ->
            InteractorResult(InteractorStatus.Success, event)
        }
    }

}