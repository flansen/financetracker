package flhan.de.financemanager.common.data

import java.util.*

data class ShoppingItem(
        val name: String = "",
        val tags: MutableList<Tag> = mutableListOf(),
        var id: String = "",
        val isChecked: Boolean = false,
        val createdAt: Date? = null
)