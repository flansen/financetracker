package flhan.de.financemanager.common.data

import java.util.*

data class ShoppingItem(
        val name: String = "",
        val creatorId: String? = null,
        val createdAt: Date = Date(),
        val tags: MutableSet<Tag> = mutableSetOf(),
        val isChecked: Boolean = false,
        var id: String = ""
)