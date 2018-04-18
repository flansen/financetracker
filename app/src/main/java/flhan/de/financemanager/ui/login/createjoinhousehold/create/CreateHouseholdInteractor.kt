package flhan.de.financemanager.ui.login.createjoinhousehold.create

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.datastore.HouseholdDataStore
import flhan.de.financemanager.common.notifications.FirebaseNotificationManager
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface CreateHouseholdInteractor {
    fun execute(name: String, secret: String): Observable<InteractorResult<Household>>
}

class CreateHouseholdInteractorImpl @Inject constructor(
        private val dataStore: HouseholdDataStore,
        private val notificationManager: FirebaseNotificationManager
) : CreateHouseholdInteractor {

    override fun execute(name: String, secret: String): Observable<InteractorResult<Household>> {
        val household = Household(name, secret = secret)

        return dataStore.createHousehold(household)
                .flatMap {
                    return@flatMap dataStore.joinHousehold(it.result!!)
                }
                //TODO: Error Handling
                .map {
                    if (it.isSuccess()) {
                        notificationManager.subscribe(it.result!!.id)
                    }
                    InteractorResult(InteractorStatus.Success, it.result!!)
                }
                .toObservable()
                .startWith(InteractorResult(InteractorStatus.Loading))
    }
}