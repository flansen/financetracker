package flhan.de.financemanager.common.extensions

import android.app.Activity
import flhan.de.financemanager.App

/**
 * Created by fhansen on 09.10.17.
 */

val Activity.app: App
    get() = application as App