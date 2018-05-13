package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.data.ShoppingItem
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.di.ShoppingItemGroupPlaceholder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.*

class ShoppingItemOverviewViewModel(
        private val interactor: ShoppingItemOverviewInteractor,
        @ShoppingItemGroupPlaceholder private val groupPlaceholderTitle: String
) : ViewModel() {

    val listItems = MutableLiveData<List<ShoppingOverviewItem>>()

    private val disposables = CompositeDisposable()

    init {
        interactor.fetchActiveItems()
                .filter { it.status == InteractorStatus.Success }
                .map { it.result ?: mutableListOf() }
                .map {
                    val groupedItems = it.groupBy { it.tag ?: groupPlaceholderTitle }
                    val mappedItems = mutableListOf<ShoppingOverviewItem>()
                    for ((groupName, items) in groupedItems) {
                        mappedItems.add(ShoppingOverviewItem.Group(groupName))
                        items.forEach {
                            mappedItems.add(ShoppingOverviewItem.Data(it.toOverviewItem()))
                        }
                    }
                    return@map mappedItems
                }
                .subscribe { listItems.value = it }
                .addTo(disposables)
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }

    fun onItemChecked(item: ShoppingOverviewItem.Data) {
        item.item.run {
            done = !done
            interactor.itemCheckedChanged(this)
                    .subscribeOn(Schedulers.io())
                    .subscribe()
                    .addTo(disposables)

        }
    }
}

private fun ShoppingItem.toOverviewItem(): ShoppingOverviewItemData {
    return ShoppingOverviewItemData(id,
            creatorId ?: "",
            name,
            createdAt.toString(),
            isChecked,
            createdAt ?: Date(),
            checkedAt,
            tag
    )
}

