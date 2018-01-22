package flhan.de.financemanager.common.extensions

import android.support.annotation.LayoutRes
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import flhan.de.financemanager.R

/**
 * Created by Florian on 03.10.2017.
 */
fun View.visible(isVisible: Boolean) {
    if (isVisible)
        this.visibility = VISIBLE
    else
        this.visibility = GONE
}

fun ActionBar.applyWhiteStyle() {
    themedContext?.theme?.applyStyle(R.style.white_control_toolbar, true)
}

infix fun ViewGroup?.inflate(@LayoutRes layoutResourceId: Int): View? {
    return this?.let { LayoutInflater.from(context).inflate(layoutResourceId, this, false) }
}