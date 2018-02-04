package flhan.de.financemanager.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import flhan.de.financemanager.R
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.base.scheduler.SchedulerProviderImpl
import flhan.de.financemanager.common.auth.AuthManager
import flhan.de.financemanager.common.auth.AuthManagerImpl
import flhan.de.financemanager.common.datastore.FirebaseClient
import flhan.de.financemanager.common.datastore.RemoteDataStore
import flhan.de.financemanager.common.datastore.UserSettings
import flhan.de.financemanager.common.datastore.UserSettingsImpl
import flhan.de.financemanager.common.notifications.FirebaseNotificationManager
import flhan.de.financemanager.common.notifications.FirebaseNotificationManagerImpl
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

    @Provides
    @Singleton
    fun notificationManger(application: Application) = application.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun firebaseNotificationManager(impl: FirebaseNotificationManagerImpl): FirebaseNotificationManager = impl

    @Provides
    @ChannelId
    fun channelId(): String = "channelid"

    @Provides
    @ChannelName
    fun channelName(context: Context): String = context.getString(R.string.app_name)
}