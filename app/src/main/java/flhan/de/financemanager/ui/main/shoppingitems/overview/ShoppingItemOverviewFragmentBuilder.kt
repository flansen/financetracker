package flhan.de.financemanager.ui.main.shoppingitems.overview

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ShoppingItemOverviewFragmentBuilder {

    @ContributesAndroidInjector(modules = [ShoppingItemOverviewModule::class])
    abstract fun provideShoppingItemOverviewFragmentFactory(): ShoppingItemOverviewFragment

}