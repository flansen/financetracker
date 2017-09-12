package flhan.de.financemanager.base

import android.app.Activity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import flhan.de.financemanager.App
import flhan.de.financemanager.R

/**
 * Created by Florian on 09.09.2017.
 */
abstract class BaseActivity : AppCompatActivity() {
    fun showErrorDialog(message: String? = null, title: String? = null) {
        val builder = AlertDialog.Builder(this)
        message ?: getString(R.string.error_generic_body)
        title ?: getString(R.string.error_generic_title)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.create().show()
    }

    fun showErrorDialog(messageId: Int, titleId: Int) {
        showErrorDialog(getString(messageId), getString(titleId))
    }
}


val Activity.app: App
    get() = application as App

