package flhan.de.financemanager.common.data

import java.util.*

data class ShoppingItem(
        var name: String = "",
        var creatorId: String? = null,
        var createdAt: Date? = null,
        var tags: MutableList<Tag> = mutableListOf(),
        var isChecked: Boolean = false,
        var id: String = "",
        var checkedAt: Date? = null
) {

    companion object {
        const val NAME = "name"
        const val CHECKED = "checked"
        const val CHECKED_AT = "checkedAt"
    }
}