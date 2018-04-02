package flhan.de.financemanager.ui.main.shoppingitems.createedit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.di.ShoppingItemId

class CreateEditShoppingItemViewModel(private val interactor: CreateEditShoppingItemInteractor,
                                      private val scheduler: SchedulerProvider,
                                      @ShoppingItemId itemId: String?
) : ViewModel() {

    val name = MutableLiveData<String>()


    fun save() {

    }
}

