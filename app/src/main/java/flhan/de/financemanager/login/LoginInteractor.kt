package flhan.de.financemanager.login

import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 09.09.2017.
 */
interface LoginInteractor {
    fun login(token: String): Observable<AuthResult>
}

class LoginInteractorImpl @Inject constructor(
        private val authManager: AuthManager
) : LoginInteractor {

    override fun login(token: String): Observable<AuthResult> {
        return authManager.auth(token)
    }
}