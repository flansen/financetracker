package flhan.de.financemanager.common.data

import java.util.*

data class ShoppingItem(
        val name: String = "",
        val tags: MutableSet<Tag> = mutableSetOf(),
        var id: String = "",
        val isChecked: Boolean = false,
        val createdAt: Date? = null
)