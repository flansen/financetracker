package flhan.de.financemanager.ui.main.shoppingitems.overview

import dagger.Module
import dagger.Provides

@Module
class ShoppingItemOverviewModule {

    @Provides
    fun fetchInteractor(interactor: ShoppingItemOverviewInteractorImpl): ShoppingItemOverviewInteractor {
        return interactor
    }
}
