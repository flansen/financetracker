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
    fun fetchActiveItems(): Observable<InteractorResult<List<ShoppingItem>>>
    fun itemCheckedChanged(item: ShoppingOverviewItemData): Observable<Unit>
}

class ShoppingItemOverviewInteractorImpl @Inject constructor(
        private val dataStore: ShoppingItemDataStore
) : ShoppingItemOverviewInteractor {

    private val minDate by lazy {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        calendar.time
    }

    override fun fetchActiveItems(): Observable<InteractorResult<List<ShoppingItem>>> {
        return dataStore.loadShoppingItems()
                .doOnNext {
                    val filteredList = it.filter {
                        it.checkedAt?.before(minDate) ?: it.isChecked
                    }
                    dataStore.removeItems(filteredList)
                }
                .map {
                    val filteredAndSortedItems = it.filter { item ->
                        !item.isChecked || item.isChecked && item.checkedAt?.after(minDate) ?: false
                    }
                            .reversed()
                            .sortedWith(compareBy<ShoppingItem> { it.isChecked }.thenByDescending { it.createdAt })

                    return@map filteredAndSortedItems
                }
                .map {
                    InteractorResult(InteractorStatus.Success, it)
                }
                .replay(1)
                .refCount()
    }

    override fun itemCheckedChanged(item: ShoppingOverviewItemData): Observable<Unit> {
        val shoppingItem = item.toModel()
        shoppingItem.checkedAt = if (shoppingItem.isChecked && shoppingItem.checkedAt == null) {
            Date()
        } else {
            null
        }
        return dataStore.saveItem(shoppingItem)
    }
}

private fun ShoppingOverviewItemData.toModel(): ShoppingItem {
    return ShoppingItem(name, creatorId, null, tag, done, id, checkedAt)
}
