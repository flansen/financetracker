package flhan.de.financemanager.main.expenseoverview

data class ExpenseOverviewItem(
        val id: String,
        val creator: String,
        val amount: String,
        val cause: String,
        val date: String)