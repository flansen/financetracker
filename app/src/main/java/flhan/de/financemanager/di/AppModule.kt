package flhan.de.financemanager.di

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.App
import flhan.de.financemanager.common.FirebaseClientImpl
import flhan.de.financemanager.common.Repository
import flhan.de.financemanager.common.RepositoryImpl
import flhan.de.financemanager.signin.AuthManager
import flhan.de.financemanager.signin.AuthManagerImpl
import javax.inject.Singleton

/**
 * Created by Florian on 10.09.2017.
 */
@Module
class AppModule(val app: App) {
    @Provides @Singleton fun app() = app

    @Provides
    @Singleton
    fun authManager(firebaseClient: FirebaseClientImpl): AuthManager = AuthManagerImpl(firebaseClient)

    @Provides
    @Singleton
    fun repository(firebaseClient: FirebaseClientImpl): Repository = RepositoryImpl(firebaseClient)

}