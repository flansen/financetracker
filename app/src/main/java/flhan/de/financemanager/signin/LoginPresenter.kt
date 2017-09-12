package flhan.de.financemanager.signin


/**
 * Created by Florian on 09.09.2017.
 */
class LoginPresenter(
        val view: LoginContract.View,
        val loginInteractor: LoginInteractor
) : LoginContract.Presenter {

    override fun startAuth(token: String) {
        loginInteractor.login(token).subscribe { result: AuthResult ->
            if (result.isSuccessful) {
            }
        }
    }

}