package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class ShoppingItemOverviewViewModel(private val interactor: ShoppingItemOverviewInteractor) : ViewModel() {

    val listItems = MutableLiveData<List<ShoppingOverviewItem>>()

    init {
        listItems.value = mutableListOf(
                ShoppingOverviewItem("id", "creatorid", "Brötchen", "heute", false),
                ShoppingOverviewItem("id", "creatorid", "Marmelade", "heute", false),
                ShoppingOverviewItem("id", "creatorid", "Obst", "heute", false),
                ShoppingOverviewItem("id", "creatorid", "Gemüse", "heute", false)
        )
    }
}

