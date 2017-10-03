package flhan.de.financemanager.createjoinhousehold

import io.reactivex.Observable
import io.reactivex.subjects.Subject

/**
 * Created by Florian on 29.09.2017.
 */
interface CreateJoinHouseholdContract {
    interface View {
        var nameObservable: Observable<CharSequence>
        var emailObservable: Observable<CharSequence>
        var stateObservable: Observable<ViewState>
        var clickSubject: Subject<Unit>

        fun dismiss()
    }

    interface Presenter {
        var canSubmitObservable: Observable<Boolean>
        var loadingObservable: Observable<Boolean>

        fun attach()
        fun detach()
    }
}