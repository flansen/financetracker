package flhan.de.financemanager.createjoinhousehold

import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.common.validators.NameValidator
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 * Created by Florian on 29.09.2017.
 */
class CreateJoinHouseholdPresenter(
        val view: CreateJoinHouseholdContract.View,
        val nameValidator: NameValidator,
        val emailValidator: EmailValidator
) : CreateJoinHouseholdContract.Presenter {
    override lateinit var canSubmitObservable: Observable<Boolean>

    override fun attach() {
        view.emailObservable.subscribe { t -> println("Mail text in Presenter $t") }
        view.nameObservable.subscribe { t -> println("Name text in Presenter $t") }

        canSubmitObservable = Observable.combineLatest(
                view.emailObservable,
                view.nameObservable,
                BiFunction { mail: CharSequence, name: CharSequence -> isValidMail(mail) || isValidName(name) })
                .distinctUntilChanged()
                .startWith(false)
    }

    private fun isValidName(name: CharSequence): Boolean {
        return nameValidator.validate(name)
    }

    private fun isValidMail(mail: CharSequence): Boolean {
        return emailValidator.validate(mail)
    }
}