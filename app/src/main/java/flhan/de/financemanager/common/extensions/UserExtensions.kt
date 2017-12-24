package flhan.de.financemanager.common.extensions

import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.ui.main.expenses.createedit.CreateEditUserItem

fun User.toListItem(): CreateEditUserItem {
    return CreateEditUserItem(name, id)
}