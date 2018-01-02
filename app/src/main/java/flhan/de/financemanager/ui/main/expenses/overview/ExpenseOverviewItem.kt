package flhan.de.financemanager.ui.main.expenses.overview

import flhan.de.financemanager.common.util.CurrencyString

data class ExpenseOverviewItem(
        val id: String,
        val creator: String,
        val amount: CurrencyString,
        val cause: String,
        val date: String)