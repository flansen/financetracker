package flhan.de.financemanager.signin

/**
 * Created by Florian on 09.09.2017.
 */
interface LoginContract {
    interface View {
        fun presentError(error: String?)
    }

    interface Presenter {
        fun startAuth(token: String)
    }
}