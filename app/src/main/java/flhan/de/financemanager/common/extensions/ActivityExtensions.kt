package flhan.de.financemanager.common.extensions

import android.app.Activity
import android.content.Intent
import flhan.de.financemanager.App
import kotlin.reflect.KClass

/**
 * Created by fhansen on 09.10.17.
 */

val Activity.app: App
    get() = application as App

fun <T : Activity> Activity.start(clazz: KClass<T>) {
    startActivity(Intent(this, clazz.java))
}