package flhan.de.financemanager.main.expenseoverview

import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.Insert
import flhan.de.financemanager.common.ListEvent
import flhan.de.financemanager.common.Remove
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.events.Create
import flhan.de.financemanager.common.events.Delete
import flhan.de.financemanager.common.events.Update
import io.reactivex.Observable

/**
 * Created by Florian on 03.10.2017.
 */
class ExpenseOverviewPresenter(
        private val view: ExpenseOverviewContract.View,
        private val fetchExpensesInteractor: FetchExpensesInteractor
) : ExpenseOverviewContract.Presenter {
    override val expenses: Observable<ListEvent<Expense>>

    init {
        expenses = fetchExpensesInteractor.fetchAll()
                .filter { result -> result.status == InteractorStatus.Success }
                .map { event ->
                    when (event.result) {
                        is Create -> {
                            val createEvent = event.result as Create<Expense>
                            return@map Insert<Expense>(createEvent.obj)
                        }
                        is Update -> {
                            val updateEvent = event.result as Update<Expense>
                            return@map flhan.de.financemanager.common.Update<Expense>(updateEvent.obj)
                        }
                        is Delete -> {
                            val deleteEvent = event.result as Delete<Expense>
                            return@map Remove<Expense>(deleteEvent.id)
                        }
                        else -> {
                            throw IllegalArgumentException("Result Type $event not supported.")
                        }
                    }
                }
    }
}