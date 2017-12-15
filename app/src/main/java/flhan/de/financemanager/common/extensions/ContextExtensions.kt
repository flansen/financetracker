package flhan.de.financemanager.common.extensions

import android.content.Context
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText

/**
 * Created by Florian on 29.09.2017.
 */
fun Context.toast(message: String) {
    makeText(this, message, LENGTH_SHORT).show()
}

fun Context.stringByName(name: String): String {
    return getString(resources.getIdentifier(name, "string", packageName))
}