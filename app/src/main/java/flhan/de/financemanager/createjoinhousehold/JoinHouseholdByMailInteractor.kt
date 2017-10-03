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
interface JoinHouseholdByMailInteractor {
    fun execute(email: String): Observable<JoinHouseholdByMailInteractor.Result>

    data class Result(
            val status: InteractorStatus,
            val result: Household? = null
    )
}

class JoinHouseholdByMailInteractorImpl @Inject constructor(
        private val dataStore: RemoteDataStore
) : JoinHouseholdByMailInteractor {
    override fun execute(email: String): Observable<JoinHouseholdByMailInteractor.Result> {
        return Observable.create<JoinHouseholdByMailInteractor.Result> { emitter: ObservableEmitter<JoinHouseholdByMailInteractor.Result> ->
            dataStore.joinHouseholdByMail(email).subscribe({
                emitter.onNext(JoinHouseholdByMailInteractor.Result(InteractorStatus.Success, it))
            }, {
                emitter.onError(it)
            })
        }.startWith(JoinHouseholdByMailInteractor.Result(InteractorStatus.Loading))
    }
}