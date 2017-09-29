package flhan.de.financemanager.signin


/**
 * Created by Florian on 09.09.2017.
 */
class LoginPresenter(
        private val view: LoginContract.View,
        private val loginInteractor: LoginInteractor,
        private val router: LoginRouter
) : LoginContract.Presenter {

    override fun startAuth(token: String) {
        loginInteractor.login(token).subscribe { result: AuthResult ->
            if (result.isSuccessful) {
                router.navigateToCreateJoinHousehold()
                view.dismiss()
            } else {
                view.presentError(result.error)
            }
        }
    }
}