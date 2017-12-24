package flhan.de.financemanager.ui.launcher

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.base.RequestResult
import flhan.de.financemanager.common.UserSettings
import flhan.de.financemanager.common.auth.AuthManager
import flhan.de.financemanager.ui.launcher.LauncherState.Initialized
import flhan.de.financemanager.ui.launcher.LauncherState.NotInitialized
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by fhansen on 06.10.17.
 */
interface CheckAuthInteractor {
    fun execute(): Observable<InteractorResult<LauncherState>>
}

class CheckAuthInteractorImpl @Inject constructor(
        private val authManager: AuthManager,
        private val userSettings: UserSettings)
    : CheckAuthInteractor {

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
                        InteractorResult(Success, Initialized)
                    else
                        InteractorResult(Success, NotInitialized)
                }
    }
}
