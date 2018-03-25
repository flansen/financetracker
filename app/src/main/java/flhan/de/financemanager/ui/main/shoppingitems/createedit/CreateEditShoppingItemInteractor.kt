package flhan.de.financemanager.ui.main.shoppingitems.createedit

import flhan.de.financemanager.common.datastore.ShoppingItemDataStore
import javax.inject.Inject

/**
 * Created by Florian on 07.10.2017.
 */
interface CreateEditShoppingItemInteractor {
}

class CreateEditShoppingItemInteractorImpl @Inject constructor(private val dataStore: ShoppingItemDataStore) : CreateEditShoppingItemInteractor {

}