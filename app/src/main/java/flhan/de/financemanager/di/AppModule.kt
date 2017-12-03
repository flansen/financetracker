package flhan.de.financemanager.di

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import flhan.de.financemanager.App
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.base.scheduler.SchedulerProviderImpl
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
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun authManager(remoteDataStore: RemoteDataStore): AuthManager = AuthManagerImpl(remoteDataStore)

    @Provides
    @Singleton
    fun userSettings(context: Context): UserSettings = UserSettingsImpl(PreferenceManager.getDefaultSharedPreferences(context))

    @Provides
    @Singleton
    fun remoteDataStore(userSettings: UserSettings): RemoteDataStore = FirebaseClient(userSettings)

    @Provides
    @Singleton
    fun schedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

}