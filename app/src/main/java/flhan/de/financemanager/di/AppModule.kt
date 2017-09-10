package flhan.de.financemanager.di

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.App
import javax.inject.Singleton

/**
 * Created by Florian on 10.09.2017.
 */
@Module
class AppModule(val app: App) {
    @Provides @Singleton fun app() = app
}