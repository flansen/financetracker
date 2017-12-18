package flhan.de.financemanager.common.extensions

import flhan.de.financemanager.common.LONG_DATE_FORMAT
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.main.expenses.overview.ExpenseOverviewItem
import java.text.SimpleDateFormat

//TODO: Formatting

fun Expense.toOverviewItem(): ExpenseOverviewItem {
    var nameString = ""
    user?.name?.split(' ')?.forEach { nameString += it[0] }
    val dateFormat = SimpleDateFormat(LONG_DATE_FORMAT)
    return ExpenseOverviewItem(id, nameString, amount.toString(), cause, dateFormat.format(createdAt))
}