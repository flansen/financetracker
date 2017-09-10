package flhan.de.financemanager

import android.app.Application
import flhan.de.financemanager.di.AppComponent
import flhan.de.financemanager.di.AppModule
import flhan.de.financemanager.di.DaggerAppComponent

/**
 * Created by Florian on 10.09.2017.
 */
class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}