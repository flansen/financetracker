package flhan.de.financemanager.di

import dagger.Module
import dagger.Provides
import flhan.de.financemanager.App
import flhan.de.financemanager.common.FirebaseClient
import flhan.de.financemanager.common.RemoteDataStore
import flhan.de.financemanager.signin.AuthManager
import flhan.de.financemanager.signin.AuthManagerImpl
import javax.inject.Singleton

/**
 * Created by Florian on 10.09.2017.
 */
@Module
class AppModule(val app: App) {
    @Provides
    @Singleton
    fun app() = app

    @Provides
    @Singleton
    fun authManager(firebaseClient: FirebaseClient): AuthManager = AuthManagerImpl(firebaseClient)

    @Provides
    @Singleton
    fun remoteDataStore(): RemoteDataStore = FirebaseClient()
}