package flhan.de.financemanager.signin

import io.reactivex.Observable

/**
 * Created by Florian on 09.09.2017.
 */
interface LoginInteractor {
    fun login(token: String): Observable<AuthResult>
}

class LoginInteractorImpl : LoginInteractor {
    val authManager: AuthManager

    init {
        authManager = AuthManagerImpl()
    }

    override fun login(token: String): Observable<AuthResult> {
        return authManager.auth(token)
    }
}