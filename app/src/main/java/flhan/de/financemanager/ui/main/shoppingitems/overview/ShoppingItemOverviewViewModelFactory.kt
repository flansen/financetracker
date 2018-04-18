package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class ShoppingItemOverviewViewModelFactory @Inject constructor(
        private val interactor: ShoppingItemOverviewInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingItemOverviewViewModel::class.java)) {
            return ShoppingItemOverviewViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}