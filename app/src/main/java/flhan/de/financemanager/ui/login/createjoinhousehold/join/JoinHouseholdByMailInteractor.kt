package flhan.de.financemanager.ui.login.createjoinhousehold.join

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.datastore.HouseholdDataStore
import flhan.de.financemanager.common.notifications.FirebaseNotificationManager
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface JoinHouseholdByMailInteractor {
    fun execute(email: String, secret: String): Observable<InteractorResult<Household>>
}

// TODO: Check secret
class JoinHouseholdByMailInteractorImpl @Inject constructor(
        private val dataStore: HouseholdDataStore,
        private val notificationManager: FirebaseNotificationManager
) : JoinHouseholdByMailInteractor {

    override fun execute(email: String, secret: String): Observable<InteractorResult<Household>> {
        return dataStore.joinHouseholdByMail(email, secret)
                .map {
                    if (it.isSuccess()) {
                        notificationManager.subscribe(it.result!!.id)
                        InteractorResult(Success, it.result)
                    } else {
                        InteractorResult<Household>(Error, null, it.exception)
                    }
                }
                .toObservable()
                .startWith(InteractorResult(Loading))
    }
}