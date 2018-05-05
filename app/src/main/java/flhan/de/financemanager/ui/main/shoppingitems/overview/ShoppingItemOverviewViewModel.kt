package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.data.ShoppingItem
import flhan.de.financemanager.common.extensions.cleanUp
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.*

class ShoppingItemOverviewViewModel(private val interactor: ShoppingItemOverviewInteractor) : ViewModel() {

    val listItems = MutableLiveData<List<ShoppingOverviewItem>>()

    private val disposables = CompositeDisposable()
    private val minDate by lazy {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -3)
        calendar.time
    }

    init {
        interactor.fetchAll()
                .filter { it.status == InteractorStatus.Success }
                .map { it.result ?: mutableListOf() }
                .map { items ->
                    val filteredList = items.filter { !it.isChecked || it.isChecked && it.checkedAt?.after(minDate) ?: false }
                    filteredList.map { it.toOverviewItem() }
                            .reversed()
                            .sortedWith(compareBy<ShoppingOverviewItem> { it.done }.thenByDescending { it.createdAt })
                }
                .subscribe { listItems.value = it }
                .addTo(disposables)
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }

    fun onItemChecked(item: ShoppingOverviewItem) {
        item.done = !item.done
        interactor.itemCheckedChanged(item)
                .subscribeOn(Schedulers.io())
                .subscribe()
                .addTo(disposables)
    }
}

private fun ShoppingItem.toOverviewItem(): ShoppingOverviewItem {
    return ShoppingOverviewItem(id, creatorId
            ?: "", name, createdAt.toString(), isChecked, createdAt ?: Date(), checkedAt)
}

