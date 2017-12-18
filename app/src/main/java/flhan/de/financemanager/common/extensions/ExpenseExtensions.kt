package flhan.de.financemanager.common.extensions

import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewItem

//TODO: Formatting
fun Expense.toOverviewItem(): ExpenseOverviewItem {
    var nameString = ""
    user?.name?.split(' ')?.forEach { nameString += it[0] }
    return ExpenseOverviewItem(id, nameString, amount.toString(), cause, createdAt.toString())
}