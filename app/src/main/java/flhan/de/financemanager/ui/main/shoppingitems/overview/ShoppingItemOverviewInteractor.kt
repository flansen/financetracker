package flhan.de.financemanager.ui.main.shoppingitems.overview

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.common.data.ShoppingItem
import flhan.de.financemanager.common.datastore.RemoteDataStore
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 07.10.2017.
 */
interface ShoppingItemOverviewInteractor {
    fun fetchAll(): Observable<InteractorResult<List<ShoppingItem>>>
}

class ShoppingItemOverviewInteractorImpl @Inject constructor(private val dataStore: RemoteDataStore
) : ShoppingItemOverviewInteractor {
    override fun fetchAll(): Observable<InteractorResult<List<ShoppingItem>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}