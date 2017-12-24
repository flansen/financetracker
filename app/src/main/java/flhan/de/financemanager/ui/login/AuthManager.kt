package flhan.de.financemanager.ui.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import flhan.de.financemanager.base.RequestResult
import flhan.de.financemanager.common.RemoteDataStore
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import javax.inject.Inject

/**
 * Created by Florian on 09.09.2017.
 */
interface AuthManager {
    fun auth(token: String): Observable<AuthResult>
    fun checkAuth(): Observable<RequestResult<Boolean>>
}

class AuthManagerImpl @Inject constructor(private val remoteDataStore: RemoteDataStore) : AuthManager {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun auth(token: String): Observable<AuthResult> {
        return Observable.create { e: ObservableEmitter<AuthResult> ->
            val credential = GoogleAuthProvider.getCredential(token, null)
            mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) remoteDataStore.init()

                e.onNext(AuthResult(task.exception.toString(), task.isSuccessful))
                e.onComplete()
            }
        }
    }

    override fun checkAuth(): Observable<RequestResult<Boolean>> {
        return Observable.fromCallable {
            return@fromCallable RequestResult(mAuth.currentUser != null)
        }
    }
}