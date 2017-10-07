package flhan.de.financemanager.common.extensions

import android.view.View

/**
 * Created by Florian on 03.10.2017.
 */
fun View.visible(isVisible: Boolean) {
    if (isVisible)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.GONE
}