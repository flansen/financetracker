package flhan.de.financemanager.di

import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import flhan.de.financemanager.App
import flhan.de.financemanager.common.FirebaseClient
import flhan.de.financemanager.common.RemoteDataStore
import flhan.de.financemanager.common.UserSettings
import flhan.de.financemanager.common.UserSettingsImpl
import flhan.de.financemanager.login.AuthManager
import flhan.de.financemanager.login.AuthManagerImpl
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
    fun authManager(remoteDataStore: RemoteDataStore): AuthManager = AuthManagerImpl(remoteDataStore)

    @Provides
    @Singleton
    fun userSettings(): UserSettings = UserSettingsImpl(PreferenceManager.getDefaultSharedPreferences(app.applicationContext))

    @Provides
    @Singleton
    fun remoteDataStore(userSettings: UserSettings): RemoteDataStore = FirebaseClient(userSettings)

}