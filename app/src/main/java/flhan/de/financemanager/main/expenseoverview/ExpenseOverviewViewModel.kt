package flhan.de.financemanager.main.expenseoverview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.events.Create
import flhan.de.financemanager.common.events.Delete
import flhan.de.financemanager.common.events.Update

class ExpenseOverviewViewModel(
        fetchExpensesInteractor: FetchExpensesInteractor,
        schedulerProvider: SchedulerProvider) : ViewModel() {

    val listItems = MutableLiveData<List<Expense>>()

    private val expenses = mutableListOf<Expense>()

    init {
        fetchExpensesInteractor.fetchAll()
                .filter { result -> result.status == Success }
                .doOnNext { event ->
                    when (event.result) {
                        is Create -> {
                            val createEvent = event.result as Create<Expense>
                            expenses.add(0, createEvent.obj)
                        }
                        is Update -> {
                            val updateEvent = event.result as Update<Expense>
                            val itemIndex = expenses.indexOfFirst { item -> item.id == updateEvent.obj.id }
                            expenses[itemIndex] = updateEvent.obj
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
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe { listItems.value = expenses }
    }
}