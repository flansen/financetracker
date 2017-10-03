package flhan.de.financemanager.createjoinhousehold

import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.RemoteDataStore
import flhan.de.financemanager.data.Household
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface JoinHouseholdInteractor {
    fun execute(household: Household): Observable<Result>

    data class Result(
            var status: InteractorStatus,
            var result: Household? = null
    )
}

class JoinHouseholdInteractorImpl @Inject constructor(
        val dataStore: RemoteDataStore
) : JoinHouseholdInteractor {
    override fun execute(household: Household): Observable<JoinHouseholdInteractor.Result> {
        return Observable.create<JoinHouseholdInteractor.Result> { emitter: ObservableEmitter<JoinHouseholdInteractor.Result> ->
            dataStore.joinHousehold(household).subscribe({
                emitter.onNext(JoinHouseholdInteractor.Result(InteractorStatus.Success, it))
            }, {
                emitter.onError(it)
            })
        }.startWith(JoinHouseholdInteractor.Result(InteractorStatus.Loading))
    }
}