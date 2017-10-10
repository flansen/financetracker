package flhan.de.financemanager.launcher

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.base.RequestResult
import flhan.de.financemanager.common.UserSettings
import flhan.de.financemanager.login.AuthManager
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by fhansen on 06.10.17.
 */
interface CheckAuthInteractor {
    fun execute(): Observable<InteractorResult<LauncherState>>
}

class CheckAuthInteractorImpl @Inject constructor(
        val authManager: AuthManager,
        val userSettings: UserSettings
) : CheckAuthInteractor {

    override fun execute(): Observable<InteractorResult<LauncherState>> {
        return userSettings
                .hasUserData()
                .toObservable()
                .flatMap { hasUserData ->
                    if (hasUserData) {
                        authManager.checkAuth()
                    } else {
                        Observable.just(RequestResult(false))
                    }
                }
                .map { authResult ->
                    if (authResult.result == true)
                        InteractorResult(InteractorStatus.Success, LauncherState.Initialized)
                    else
                        InteractorResult(InteractorStatus.Success, LauncherState.NotInitialized)
                }
    }
}
