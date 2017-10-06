package flhan.de.financemanager.login

/**
 * Created by Florian on 09.09.2017.
 */
interface LoginContract {
    interface View {
        fun presentError(error: String?)
        fun dismiss()
    }

    interface Presenter {
        fun startAuth(token: String)
    }
}