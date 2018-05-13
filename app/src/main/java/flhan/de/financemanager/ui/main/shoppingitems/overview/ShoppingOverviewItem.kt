package flhan.de.financemanager.ui.main.shoppingitems.overview

import java.util.*

sealed class ShoppingOverviewItem {
    class Data(val item: ShoppingOverviewItemData) : ShoppingOverviewItem()
    class Group(val name: String) : ShoppingOverviewItem()
}

data class ShoppingOverviewItemData(
        val id: String,
        val creatorId: String,
        val name: String,
        val date: String,
        var done: Boolean,
        var createdAt: Date,
        var checkedAt: Date?,
        var tag: String?
)