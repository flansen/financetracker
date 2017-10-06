package flhan.de.financemanager.launcher

import io.reactivex.Observable

/**
 * Created by fhansen on 06.10.17.
 */
interface LauncherContract {
    interface View

    interface Presenter {
        fun attach()
        val shouldPresentLogin: Observable<Boolean>
    }
}