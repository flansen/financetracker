package flhan.de.financemanager.ui.main.shoppingitems.createedit

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.di.ShoppingItemId
import javax.inject.Inject

class CreateEditShoppingItemViewModelFactory @Inject constructor(
        private val interactor: CreateEditShoppingItemInteractor,
        private val scheduler: SchedulerProvider,
        @ShoppingItemId private val id: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEditShoppingItemViewModel::class.java)) {
            return CreateEditShoppingItemViewModel(interactor, scheduler, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}