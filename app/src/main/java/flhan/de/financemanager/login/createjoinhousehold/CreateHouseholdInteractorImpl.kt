package flhan.de.financemanager.login.createjoinhousehold

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.RemoteDataStore
import flhan.de.financemanager.common.data.Household
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface CreateHouseholdInteractor {
    fun execute(name: String): Observable<InteractorResult<Household>>

}

class CreateHouseholdInteractorImpl @Inject constructor(
        val dataStore: RemoteDataStore
) : CreateHouseholdInteractor {
    override fun execute(name: String): Observable<InteractorResult<Household>> {
        val household = Household(name)

        return dataStore.createHousehold(household)
                .flatMap {
                    return@flatMap dataStore.joinHousehold(it.result!!)
                }
                .map {
                    InteractorResult<Household>(InteractorStatus.Success, it.result!!)
                }
                .toObservable()
                .startWith(InteractorResult(InteractorStatus.Loading))
    }
}