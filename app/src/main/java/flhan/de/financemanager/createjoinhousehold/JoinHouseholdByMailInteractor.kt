package flhan.de.financemanager.createjoinhousehold

import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.RemoteDataStore
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface JoinHouseholdByMailInteractor {
    fun execute(email: String): Observable<CreateHouseholdInteractor.Result>
}

class JoinHouseholdByMailInteractorImpl @Inject constructor(
        private val dataStore: RemoteDataStore
) : JoinHouseholdByMailInteractor {
    //TODO: Extract the Interactor Result Type
    override fun execute(email: String): Observable<CreateHouseholdInteractor.Result> {
        return dataStore.joinHouseholdByMail(email)
                .map {
                    CreateHouseholdInteractor.Result(InteractorStatus.Success, it)
                }
                .toObservable()
                .startWith(CreateHouseholdInteractor.Result(InteractorStatus.Loading))
    }
}