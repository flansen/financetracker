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
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.common.datastore.*
import flhan.de.financemanager.common.notifications.FirebaseNotificationManager
import flhan.de.financemanager.common.notifications.FirebaseNotificationManagerImpl
import io.reactivex.Observable
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
    fun householdDataStore(householdDataStore: HouseholdDataStoreImpl): HouseholdDataStore = householdDataStore

    @Provides
    @Singleton
    fun expenseDataStore(store: ExpenseDataStoreImpl): ExpenseDataStore = store

    @Provides
    @Singleton
    fun userDataStore(store: UserDataStoreImpl): UserDataStore = store

    @Provides
    @Singleton
    fun shoppingItemDataStore(store: ShoppingItemDataStoreImpl): ShoppingItemDataStore = store

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
    @UserId
    fun userId(userSettings: UserSettings) = userSettings.getUserId()

    @Provides
    @HouseholdId
    fun householdId(userSettings: UserSettings) = userSettings.getHouseholdId()

    @Provides
    @ChannelName
    fun channelName(context: Context): String = context.getString(R.string.app_name)

    @Provides
    fun usersObservable(userDataStore: UserDataStore): Observable<MutableList<User>> {
        return userDataStore.loadUsers()
    }
}