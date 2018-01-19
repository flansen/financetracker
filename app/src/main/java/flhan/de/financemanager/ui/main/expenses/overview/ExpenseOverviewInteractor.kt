package flhan.de.financemanager.ui.main.expenses.overview

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.datastore.RemoteDataStore
import flhan.de.financemanager.common.util.CurrencyString
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 07.10.2017.
 */
interface ExpenseOverviewInteractor {
    fun fetchAll(): Observable<InteractorResult<List<Expense>>>
    fun getPaymentItems(): Observable<List<ExpensePaymentItem>>
}

class ExpenseOverviewInteractorImpl @Inject constructor(private val dataStore: RemoteDataStore
) : ExpenseOverviewInteractor {

    override fun getPaymentItems(): Observable<List<ExpensePaymentItem>> {
        return fetchAll()
                .filter { it.status == Success && it.result != null }
                .map { it.result!! }
                .map { expenseList ->
                    val expensesByCreator = expenseList.groupBy { it.user }.filterKeys { it != null }
                    return@map expensesByCreator.mapValues { expenseEntry ->
                        val amount = expenseEntry.value.sumByDouble { it.amount ?: 0.0 }
                        CurrencyString(amount)
                    }.map {
                        val user = it.key!!
                        ExpensePaymentItem(user.id, user.displayName, it.value.displayString)
                    }
                }
    }

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