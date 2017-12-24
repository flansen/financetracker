package flhan.de.financemanager.ui.login

import flhan.de.financemanager.common.auth.AuthManager
import flhan.de.financemanager.common.auth.AuthResult
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 09.09.2017.
 */
interface LoginInteractor {
    fun login(token: String): Observable<AuthResult>
}

class LoginInteractorImpl @Inject constructor(private val authManager: AuthManager) : LoginInteractor {

    override fun login(token: String): Observable<AuthResult> {
        return authManager.auth(token)
    }
}