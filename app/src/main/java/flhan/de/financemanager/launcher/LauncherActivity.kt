package flhan.de.financemanager.launcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import flhan.de.financemanager.base.app
import flhan.de.financemanager.di.launcher.LauncherModule

class LauncherActivity : AppCompatActivity(), LauncherContract.View {
    private val component by lazy { app.appComponent.plus(LauncherModule(this)) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }
}
