package flhan.de.financemanager.login.createjoinhousehold

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.RemoteDataStore
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.events.Create
import flhan.de.financemanager.common.events.Delete
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface JoinHouseholdByMailInteractor {
    fun execute(email: String): Observable<InteractorResult<Household>>
}

class JoinHouseholdByMailInteractorImpl @Inject constructor(
        private val dataStore: RemoteDataStore
) : JoinHouseholdByMailInteractor {
    override fun execute(email: String): Observable<InteractorResult<Household>> {
        dataStore.loadExpenses().subscribe { event ->
            when (event) {
                is Create -> {
                    val o = event.obj
                }
                is Delete -> {
                    event.id
                }
            }
        }

        return dataStore.joinHouseholdByMail(email)
                .map {
                    if (it.exception == null) {
                        InteractorResult(InteractorStatus.Success, it.result)
                    } else {
                        InteractorResult<Household>(InteractorStatus.Error, null, it.exception)
                    }
                }
                .toObservable()
                .startWith(InteractorResult(InteractorStatus.Loading))
    }
}