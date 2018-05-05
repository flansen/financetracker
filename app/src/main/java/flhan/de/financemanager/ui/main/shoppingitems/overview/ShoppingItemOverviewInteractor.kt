package flhan.de.financemanager.ui.main.shoppingitems.overview

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.data.ShoppingItem
import flhan.de.financemanager.common.datastore.ShoppingItemDataStore
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by Florian on 07.10.2017.
 */
interface ShoppingItemOverviewInteractor {
    fun fetchAll(): Observable<InteractorResult<List<ShoppingItem>>>
    fun itemCheckedChanged(item: ShoppingOverviewItem): Observable<Unit>
}

class ShoppingItemOverviewInteractorImpl @Inject constructor(private val dataStore: ShoppingItemDataStore
) : ShoppingItemOverviewInteractor {

    override fun fetchAll(): Observable<InteractorResult<List<ShoppingItem>>> {
        return dataStore
                .loadShoppingItems()
                .map { itemList ->
                    InteractorResult(InteractorStatus.Success, itemList.toList())
                }
                .replay(1)
                .refCount()
    }

    override fun itemCheckedChanged(item: ShoppingOverviewItem): Observable<Unit> {
        val shoppingItem = item.toModel()
        shoppingItem.checkedAt = if (shoppingItem.isChecked && shoppingItem.checkedAt == null) {
            Date()
        } else {
            null
        }
        return dataStore.saveItem(shoppingItem)
    }
}

private fun ShoppingOverviewItem.toModel(): ShoppingItem {
    return ShoppingItem(name, creatorId, null, mutableListOf(), done, id, checkedAt)
}
