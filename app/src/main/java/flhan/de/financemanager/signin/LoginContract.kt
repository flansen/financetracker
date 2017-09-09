package flhan.de.financemanager.signin

/**
 * Created by Florian on 09.09.2017.
 */
interface LoginContract {
    interface View

    interface Presenter {
        fun loginClicked()
    }
}