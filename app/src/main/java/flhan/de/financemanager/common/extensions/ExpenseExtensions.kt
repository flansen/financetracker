package flhan.de.financemanager.common.extensions

import flhan.de.financemanager.common.LONG_DATE_FORMAT
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.util.CurrencyString
import flhan.de.financemanager.ui.main.expenses.overview.ExpenseOverviewItem
import java.text.SimpleDateFormat

fun Expense.toOverviewItem(): ExpenseOverviewItem {
    val dateFormat = SimpleDateFormat.getInstance() as SimpleDateFormat
    dateFormat.applyPattern(LONG_DATE_FORMAT)
    return ExpenseOverviewItem(id, user!!.displayName, user!!.id, CurrencyString(amount), cause, dateFormat.format(createdAt))
}