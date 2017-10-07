package flhan.de.financemanager.common.extensions

import android.content.Context
import android.widget.Toast

/**
 * Created by Florian on 29.09.2017.
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.stringByName(name: String): String {
    return getString(resources.getIdentifier(name, "string", packageName))
}