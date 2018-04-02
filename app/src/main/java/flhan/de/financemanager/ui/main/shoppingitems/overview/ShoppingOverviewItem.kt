package flhan.de.financemanager.ui.main.shoppingitems.overview

data class ShoppingOverviewItem(
        val id: String,
        val creatorId: String,
        val name: String,
        val date: String,
        var done: Boolean
)