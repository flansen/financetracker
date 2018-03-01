package flhan.de.financemanager.ui.main.expenses.overview

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Error
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.Billing
import flhan.de.financemanager.common.data.BillingItem
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.common.datastore.UserExpenseDataStore
import flhan.de.financemanager.common.util.CurrencyString
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by Florian on 07.10.2017.
 */
interface ExpenseOverviewInteractor {
    fun fetchAll(): Observable<InteractorResult<List<Expense>>>
    fun calculatePaymentItems(): Observable<List<ExpensePaymentItem>>
    fun billAll(): Observable<InteractorResult<Unit>>
}

class ExpenseOverviewInteractorImpl @Inject constructor(private val dataStore: UserExpenseDataStore
) : ExpenseOverviewInteractor {

    override fun calculatePaymentItems(): Observable<List<ExpensePaymentItem>> {
        return fetchAll()
                .filter { it.status == Success && it.result != null }
                .map { it.result!! }
                .map { expenseList ->
                    convertExpensesToMapByUser(expenseList).map {
                        val user = it.key!!
                        val currencyString = CurrencyString(it.value)
                        ExpensePaymentItem(user.id, user.displayName, currencyString.displayString)
                    }
                }
                .replay(1)
                .refCount()
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

    override fun billAll(): Observable<InteractorResult<Unit>> {
        val saveResult = fetchAll()
                .filter { it.status == Success && it.result != null }
                .map { it.result!! }
                .take(1)
                .map {
                    val expenseMap = convertExpensesToMapByUser(it)
                    val billingItems = expenseMap.mapValues {
                        BillingItem(it.key!!.id, it.value, Date())
                    }.values.toList()
                    val currentUserId = dataStore.getCurrentUser().id
                    Billing(billingItems, Date(), currentUserId)
                }
                .flatMap { dataStore.saveBilling(it).toObservable() }
                .share()


        val saveError = saveResult
                .filter { it.exception != null }
                .map { InteractorResult<Unit>(Error, null, it.exception) }

        val clearResult = saveResult
                .filter { it.exception == null }
                .flatMap { dataStore.deleteExpenses().toObservable() }
                .map { success ->
                    if (success) {
                        InteractorResult(Success, Unit)
                    } else {
                        InteractorResult(Error)
                    }
                }
        return Observable.merge(saveError, clearResult)
    }

    private fun convertExpensesToMapByUser(expenseList: List<Expense>): Map<User?, Double> {
        val expensesByCreator = expenseList.groupBy { it.user }.filterKeys { it != null }
        return expensesByCreator.mapValues { expenseEntry ->
            expenseEntry.value.sumByDouble { it.amount ?: 0.0 }
        }
    }
}