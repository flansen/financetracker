package flhan.de.financemanager.signin

import io.reactivex.Observable

/**
 * Created by Florian on 09.09.2017.
 */
interface LoginContract {
    interface View {
    }

    interface Presenter {
        fun startAuth(token: String)
    }
}