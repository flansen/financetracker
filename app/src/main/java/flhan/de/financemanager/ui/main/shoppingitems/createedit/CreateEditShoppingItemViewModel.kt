package flhan.de.financemanager.ui.main.shoppingitems.createedit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.CreateEditMode
import flhan.de.financemanager.common.CreateEditMode.Create
import flhan.de.financemanager.common.CreateEditMode.Edit
import flhan.de.financemanager.common.data.ShoppingItem
import flhan.de.financemanager.common.data.Tag
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.di.ShoppingItemId
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*

class CreateEditShoppingItemViewModel(
        private val interactor: CreateEditShoppingItemInteractor,
        private val scheduler: SchedulerProvider,
        @ShoppingItemId private val itemId: String?
) : ViewModel() {

    val itemName = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val mode = MutableLiveData<CreateEditMode>()
    val tags = MutableLiveData<List<TagItem>>()
    val selectedTag = MutableLiveData<String>()

    private var item: ShoppingItem? = null
    private val disposables = CompositeDisposable()

    init {
        val itemObservable: Observable<InteractorResult<ShoppingItem>>
        isLoading.value = false

        if (itemId.isNullOrEmpty()) {
            mode.value = Create
            itemObservable = Observable.just(InteractorResult(Success, ShoppingItem()))
        } else {
            mode.value = Edit
            itemObservable = interactor.findById(itemId!!)
        }

        itemObservable
                .subscribeOn(scheduler.io())
                .replay(1)
                .refCount()

        val loadingObservable: Observable<Boolean> = itemObservable.map { result ->
            return@map result.status == Loading
        }

        loadingObservable
                .observeOn(scheduler.main())
                .subscribe { isLoading.value = it }
                .addTo(disposables)

        itemObservable
                .filter { it.status == Success }
                .subscribe { fetchResult ->
                    item = fetchResult.result
                    item?.run {
                        itemName.value = name
                        selectedTag.value = tag
                    }
                }
                .addTo(disposables)

        interactor.getTags()
                .subscribe {
                    it.result?.run {
                        tags.value = sortedWith(compareByDescending { it.numberOfOccurence }).map { it.toItem() }
                    }
                }
                .addTo(disposables)
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }

    fun save(success: () -> Unit) {
        val itemName = itemName.value ?: return
        val item = item ?: return
        item.apply {
            name = itemName.trim()
            createdAt = createdAt ?: Date()
            tag = selectedTag.value
        }

        interactor.save(item)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.main())
                .subscribe { result ->
                    isLoading.value = result.status == Loading
                    if (result.status == Success) {
                        success()
                    } else if (result.status == Error) {
                        handleError(result.exception!!)
                    }
                }
    }

    private fun handleError(exception: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

private fun Tag.toItem() = TagItem(name, id)
