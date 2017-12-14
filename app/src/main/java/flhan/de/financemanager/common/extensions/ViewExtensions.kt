package flhan.de.financemanager.common.extensions

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * Created by Florian on 03.10.2017.
 */
fun View.visible(isVisible: Boolean) {
    if (isVisible)
        this.visibility = VISIBLE
    else
        this.visibility = GONE
}