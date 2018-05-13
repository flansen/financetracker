package flhan.de.financemanager.ui.main.shoppingitems.overview

import java.util.*

data class ShoppingOverviewItem(
        val id: String,
        val creatorId: String,
        val name: String,
        val date: String,
        var done: Boolean,
        var createdAt: Date,
        var checkedAt: Date?,
        var tag: String?
)