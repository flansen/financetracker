package flhan.de.financemanager.launcher

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.UserSettings
import flhan.de.financemanager.login.AuthManager
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by fhansen on 06.10.17.
 */
interface CheckAuthInteractor {
    fun execute(): Observable<InteractorResult<Boolean>>
}

class CheckAuthInteractorImpl @Inject constructor(
        val authManager: AuthManager,
        val userSettings: UserSettings
) : CheckAuthInteractor {

    override fun execute(): Observable<InteractorResult<Boolean>> {
        return authManager
                .checkAuth()
                .flatMap { result ->
                    if (result.result!!) {
                        userSettings.hasUserData().map { hasUserData ->
                            InteractorResult(InteractorStatus.Success, hasUserData)
                        }.toObservable()
                    } else {
                        Observable.just(InteractorResult(InteractorStatus.Success, false))
                    }
                }
                .startWith { InteractorResult<Boolean>(InteractorStatus.Loading) }
                .share()
    }
}
