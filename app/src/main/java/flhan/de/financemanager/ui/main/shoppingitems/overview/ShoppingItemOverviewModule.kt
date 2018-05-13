package flhan.de.financemanager.ui.main.shoppingitems.overview

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.R
import flhan.de.financemanager.di.ShoppingItemGroupPlaceholder

@Module
class ShoppingItemOverviewModule {

    @Provides
    @ShoppingItemGroupPlaceholder
    fun placeholderString(fragment: ShoppingItemOverviewFragment): String = fragment.getString(R.string.shopping_item_group_placeholder)

    @Provides
    fun fetchInteractor(interactor: ShoppingItemOverviewInteractorImpl): ShoppingItemOverviewInteractor {
        return interactor
    }
}
