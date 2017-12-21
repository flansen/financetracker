package flhan.de.financemanager.ui.main.expenses.overview

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Success
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

class FetchExpensesInteractorImpl @Inject constructor(private val dataStore: RemoteDataStore)
    : FetchExpensesInteractor {

    override fun fetchAll(): Observable<InteractorResult<RepositoryEvent<Expense>>> {
        return dataStore
                .loadExpenses()
                .map { event ->
                    InteractorResult(Success, event)
                }
    }
}