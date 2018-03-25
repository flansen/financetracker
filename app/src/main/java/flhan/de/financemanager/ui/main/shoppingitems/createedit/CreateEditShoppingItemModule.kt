package flhan.de.financemanager.ui.main.shoppingitems.createedit

import dagger.Module
import dagger.Provides

@Module
class CreateEditShoppingItemModule {

    @Provides
    fun interactor(interactor: CreateEditShoppingItemInteractorImpl): CreateEditShoppingItemInteractor = interactor
}