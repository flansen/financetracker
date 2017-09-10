package flhan.de.financemanager.base

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import flhan.de.financemanager.App

/**
 * Created by Florian on 09.09.2017.
 */
abstract class BaseActivity : AppCompatActivity() {
}


val Activity.app: App
    get() = application as App

