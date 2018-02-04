package flhan.de.financemanager.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import flhan.de.financemanager.common.notifications.FirebaseMessagingServiceImpl

@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract fun firebaseService(): FirebaseMessagingServiceImpl
}