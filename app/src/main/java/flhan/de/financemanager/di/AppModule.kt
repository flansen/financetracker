package flhan.de.financemanager.di

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.base.scheduler.SchedulerProviderImpl
import flhan.de.financemanager.common.FirebaseClient
import flhan.de.financemanager.common.RemoteDataStore
import flhan.de.financemanager.common.UserSettings
import flhan.de.financemanager.common.UserSettingsImpl
import flhan.de.financemanager.ui.login.AuthManager
import flhan.de.financemanager.ui.login.AuthManagerImpl
import javax.inject.Singleton

/**
 * Created by Florian on 10.09.2017.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun authManager(authManager: AuthManagerImpl): AuthManager = authManager

    @Provides
    @Singleton
    fun userSettings(userSettings: UserSettingsImpl): UserSettings = userSettings

    @Provides
    @Singleton
    fun remoteDataStore(firebaseClient: FirebaseClient): RemoteDataStore = firebaseClient

    @Provides
    @Singleton
    fun sharedPrefs(application: Application) = PreferenceManager.getDefaultSharedPreferences(application)

    @Provides
    @Singleton
    fun schedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

}