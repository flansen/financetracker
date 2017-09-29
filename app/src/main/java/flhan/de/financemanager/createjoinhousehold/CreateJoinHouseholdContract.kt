package flhan.de.financemanager.createjoinhousehold

import io.reactivex.Observable

/**
 * Created by Florian on 29.09.2017.
 */
interface CreateJoinHouseholdContract {
    interface View {
        var nameObservable: Observable<CharSequence>
        var emailObservable: Observable<CharSequence>
    }

    interface Presenter {
        var canSubmitObservable: Observable<Boolean>

        fun attach()
    }
}