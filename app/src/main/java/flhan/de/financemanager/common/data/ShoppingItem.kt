package flhan.de.financemanager.common.data

import java.util.*

data class ShoppingItem(
        var name: String = "",
        var creatorId: String? = null,
        var createdAt: Date? = null,
        var tag: String? = null,
        var isChecked: Boolean = false,
        var id: String = "",
        var checkedAt: Date? = null
) {

    companion object {
        const val NAME = "name"
        const val CHECKED = "checked"
        const val CHECKED_AT = "checkedAt"
        const val TAG = "tag"
    }
}