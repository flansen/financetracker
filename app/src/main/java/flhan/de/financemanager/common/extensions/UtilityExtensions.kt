package flhan.de.financemanager.common.extensions

import android.content.res.Resources


/**
 * Created by Florian on 06.10.2017.
 */
fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().getDisplayMetrics().density).toInt()
}

fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().getDisplayMetrics().density).toInt()
}