package flhan.de.financemanager.ui.main.expenses.overview

data class ExpenseOverviewItem(
        val id: String,
        val creator: String,
        val amount: String,
        val cause: String,
        val date: String)