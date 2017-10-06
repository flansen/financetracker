package flhan.de.financemanager.login

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import io.reactivex.Observable

/**
 * Created by fhansen on 06.10.17.
 */
interface CheckAuthInteractor {
    fun execute(): Observable<InteractorResult<Boolean>>
}

class CheckAuthInteractorImpl(
        val authManager: AuthManager
) : CheckAuthInteractor {

    override fun execute(): Observable<InteractorResult<Boolean>> {
        return authManager
                .checkAuth()
                .map { InteractorResult(InteractorStatus.Success, it.result) }
                .startWith { InteractorResult<Boolean>(InteractorStatus.Loading) }
    }
}
