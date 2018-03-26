package flhan.de.financemanager.ui.main.shoppingitems.createedit

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.di.ShoppingItemId

@Module
class CreateEditShoppingItemModule {

    @Provides
    fun interactor(interactor: CreateEditShoppingItemInteractorImpl): CreateEditShoppingItemInteractor = interactor

    @Provides
    @ShoppingItemId
    fun providesId(activity: CreateEditShoppingItemActivity): String? = activity.id

}