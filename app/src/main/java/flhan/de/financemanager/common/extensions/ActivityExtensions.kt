package flhan.de.financemanager.common.extensions

import android.app.Activity
import android.content.Intent
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
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

fun Activity.goUp() {
    NavUtils.navigateUpFromSameTask(this)
}

fun Activity.hideKeyboard() {
    currentFocus?.also {
        val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}