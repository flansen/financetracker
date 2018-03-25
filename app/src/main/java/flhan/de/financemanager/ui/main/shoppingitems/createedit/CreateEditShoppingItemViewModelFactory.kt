package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.ui.main.shoppingitems.createedit.CreateEditShoppingItemInteractor
import flhan.de.financemanager.ui.main.shoppingitems.createedit.CreateEditShoppingItemViewModel
import javax.inject.Inject

class CreateEditShoppingItemViewModelFactory @Inject constructor(
        private val interactor: CreateEditShoppingItemInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditShoppingItemViewModel::class.java)) {
            return CreateEditShoppingItemViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}