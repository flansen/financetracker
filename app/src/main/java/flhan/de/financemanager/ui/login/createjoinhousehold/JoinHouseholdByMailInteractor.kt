package flhan.de.financemanager.ui.login.createjoinhousehold

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.datastore.RemoteDataStore
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface JoinHouseholdByMailInteractor {
    fun execute(email: String): Observable<InteractorResult<Household>>
}

class JoinHouseholdByMailInteractorImpl @Inject constructor(private val dataStore: RemoteDataStore)
    : JoinHouseholdByMailInteractor {

    override fun execute(email: String): Observable<InteractorResult<Household>> {
        return dataStore.joinHouseholdByMail(email)
                .map {
                    if (it.exception == null) {
                        InteractorResult(Success, it.result)
                    } else {
                        InteractorResult<Household>(Error, null, it.exception)
                    }
                }
                .toObservable()
                .startWith(InteractorResult(Loading))
    }
}