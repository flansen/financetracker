package flhan.de.financemanager.ui.main.expenses.overview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.datastore.Create
import flhan.de.financemanager.common.datastore.Delete
import flhan.de.financemanager.common.datastore.Update
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.common.extensions.toOverviewItem
import flhan.de.financemanager.common.util.CurrencyString
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ExpenseOverviewViewModel(fetchExpensesInteractor: FetchExpensesInteractor) : ViewModel() {

    val listItems = MutableLiveData<List<ExpenseOverviewItem>>()
    val paymentSums: LiveData<List<ExpensePaymentItem>>

    private val expenses = mutableListOf<ExpenseOverviewItem>()
    private val disposables = CompositeDisposable()

    init {
        fetchExpensesInteractor.fetchAll()
                .filter { result -> result.status == Success }
                .doOnNext { event ->
                    when (event.result) {
                        is Create -> {
                            val createEvent = event.result as Create<Expense>
                            expenses.add(0, createEvent.obj.toOverviewItem())
                        }
                        is Update -> {
                            val updateEvent = event.result as Update<Expense>
                            val itemIndex = expenses.indexOfFirst { item -> item.id == updateEvent.obj.id }
                            expenses[itemIndex] = updateEvent.obj.toOverviewItem()
                        }
                        is Delete -> {
                            val deleteEvent = event.result as Delete<Expense>
                            val itemIndex = expenses.indexOfFirst { item -> item.id == deleteEvent.id }
                            expenses.removeAt(itemIndex)
                        }
                        else -> {
                            throw IllegalArgumentException("Result Type $event not supported.")
                        }
                    }
                }
                .subscribe { listItems.value = expenses }
                .addTo(disposables)

        paymentSums = Transformations.map(listItems, { items ->
            return@map items
                    .groupBy { it.creatorId }
                    .mapValues {
                        val amount = it.value.sumByDouble { it.amount.amount ?: 0.0 }
                        CurrencyString(amount)
                    }
                    .map { entry ->
                        val item = items.firstOrNull { it.creatorId == entry.key }
                        ExpensePaymentItem(item?.id ?: "", item?.creator ?: "", entry.value.displayString)
                    }
        })
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }
}

