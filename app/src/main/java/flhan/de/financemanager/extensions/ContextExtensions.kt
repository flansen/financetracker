package flhan.de.financemanager.extensions

import android.content.Context
import android.widget.Toast

/**
 * Created by Florian on 29.09.2017.
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}