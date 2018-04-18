package flhan.de.financemanager.ui.main.shoppingitems.createedit

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.data.ShoppingItem
import flhan.de.financemanager.common.datastore.ShoppingItemDataStore
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 07.10.2017.
 */
interface CreateEditShoppingItemInteractor {
    fun save(item: ShoppingItem): Observable<InteractorResult<Unit>>
    fun findById(itemId: String): Observable<InteractorResult<ShoppingItem>>
}

class CreateEditShoppingItemInteractorImpl @Inject constructor(private val dataStore: ShoppingItemDataStore) : CreateEditShoppingItemInteractor {

    override fun findById(itemId: String): Observable<InteractorResult<ShoppingItem>> {
        return dataStore.findById(itemId)
                .filter { it.exception == null }
                .map { InteractorResult(InteractorStatus.Success, it.result!!) }
                .startWith(InteractorResult(InteractorStatus.Loading))
    }

    override fun save(item: ShoppingItem): Observable<InteractorResult<Unit>> {
        return dataStore
                .saveItem(item)
                .map { InteractorResult(InteractorStatus.Success, it, null) }
                .startWith(InteractorResult(InteractorStatus.Loading))
                .onErrorResumeNext { error: Throwable -> Observable.just(InteractorResult<Unit>(InteractorStatus.Error, null, error)) }
    }
}