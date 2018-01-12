package flhan.de.financemanager.ui.main.expenses.overview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.common.extensions.toOverviewItem
import flhan.de.financemanager.common.util.CurrencyString
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ExpenseOverviewViewModel(fetchExpensesInteractor: FetchExpensesInteractor) : ViewModel() {

    val listItems = MutableLiveData<List<ExpenseOverviewItem>>()
    val paymentSums: LiveData<List<ExpensePaymentItem>>

    private val disposables = CompositeDisposable()

    init {
        fetchExpensesInteractor.fetchAll()
                .filter { result -> result.status == Success }
                .map { expenses ->
                    expenses.result?.map { it.toOverviewItem() } ?: listOf()
                }
                .subscribe { listItems.value = it }
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
                        ExpensePaymentItem(item?.creatorId ?: "", item?.creator ?: "", entry.value.displayString)
                    }
        })
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }
}

