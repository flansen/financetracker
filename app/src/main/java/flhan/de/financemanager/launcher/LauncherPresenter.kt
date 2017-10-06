package flhan.de.financemanager.launcher

import io.reactivex.Observable

/**
 * Created by fhansen on 06.10.17.
 */
class LauncherPresenter(
        val interactor: CheckAuthInteractor
) : LauncherContract.Presenter {
    override lateinit var shouldPresentLogin: Observable<Boolean>

    override fun attach() {
        shouldPresentLogin = interactor.execute()
                .map { it.result != false }
    }

}