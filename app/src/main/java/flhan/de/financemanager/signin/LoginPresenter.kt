package flhan.de.financemanager.signin

import io.reactivex.Observable
import io.reactivex.functions.Consumer


/**
 * Created by Florian on 09.09.2017.
 */
class LoginPresenter(
        val view : LoginContract.View
) : LoginContract.Presenter {
    val loginInteractor: LoginInteractor

    init {
        loginInteractor = LoginInteractorImpl()
    }

    override fun startAuth(token: String) {
        loginInteractor.login(token).subscribe({ result: AuthResult ->
            if(result.isSuccessful) {
            }
        })
    }

}