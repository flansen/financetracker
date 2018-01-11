package flhan.de.financemanager.common.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup

/**
 * Created by Florian on 03.10.2017.
 */
fun View.visible(isVisible: Boolean) {
    if (isVisible)
        this.visibility = VISIBLE
    else
        this.visibility = GONE
}

infix fun ViewGroup.inflate(@LayoutRes layoutResourceId: Int): View {
    return LayoutInflater.from(context).inflate(layoutResourceId, this, false)
}