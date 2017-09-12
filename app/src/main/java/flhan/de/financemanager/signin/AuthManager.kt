package flhan.de.financemanager.signin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/**
 * Created by Florian on 09.09.2017.
 */
interface AuthManager {
    fun auth(token: String): Observable<AuthResult>
}

class AuthManagerImpl : AuthManager {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun auth(token: String): Observable<AuthResult> {
        return Observable.create { e: ObservableEmitter<AuthResult> ->
            val credential = GoogleAuthProvider.getCredential(token, null)
            mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                e.onNext(AuthResult(task.exception.toString(), task.isSuccessful))
            }
        }
    }
}