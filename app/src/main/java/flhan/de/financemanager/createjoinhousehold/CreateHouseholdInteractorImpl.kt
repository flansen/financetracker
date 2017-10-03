package flhan.de.financemanager.createjoinhousehold

import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.RemoteDataStore
import flhan.de.financemanager.data.Household
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface CreateHouseholdInteractor {
    fun execute(name: String): Observable<Result>

    data class Result(
            var status: InteractorStatus,
            var result: Household? = null
    )
}

class CreateHouseholdInteractorImpl @Inject constructor(
        val dataStore: RemoteDataStore
) : CreateHouseholdInteractor {
    override fun execute(name: String): Observable<CreateHouseholdInteractor.Result> {
        val household = Household(name)

        return dataStore.createHousehold(household)
                .flatMap { h: Household -> dataStore.joinHousehold(h) }
                .map {
                    CreateHouseholdInteractor.Result(InteractorStatus.Success, it)
                }
                .toObservable()
                .startWith(CreateHouseholdInteractor.Result(InteractorStatus.Loading))
    }
}