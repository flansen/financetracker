package flhan.de.financemanager.main.expenseoverview

import flhan.de.financemanager.common.ListEvent
import flhan.de.financemanager.common.data.Expense
import io.reactivex.Observable

/**
 * Created by Florian on 03.10.2017.
 */
interface ExpenseOverviewContract {
    interface View

    interface Presenter {
        val expenses: Observable<ListEvent<Expense>>
    }
}