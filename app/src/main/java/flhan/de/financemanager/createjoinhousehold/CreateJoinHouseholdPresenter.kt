package flhan.de.financemanager.createjoinhousehold

import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.common.validators.NameValidator
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

/**
 * Created by Florian on 29.09.2017.
 */
class CreateJoinHouseholdPresenter(
        val view: CreateJoinHouseholdContract.View,
        val nameValidator: NameValidator,
        val emailValidator: EmailValidator,
        val createHouseholdInteractor: CreateHouseholdInteractor
) : CreateJoinHouseholdContract.Presenter {

    override lateinit var canSubmitObservable: Observable<Boolean>

    private val disposables = CompositeDisposable()
    private var viewState: ViewState? = null

    override fun attach() {
        canSubmitObservable = view.stateObservable.map {
            when (it.inputState) {
                InputState.Create -> return@map isValidName(it.text)
                InputState.Join -> return@map isValidMail(it.text)
                else -> return@map false
            }
        }

        view.stateObservable.subscribe {
            viewState = it
        }.addTo(disposables)
    }

    override fun onDoneClick() {
        if (viewState!!.inputState == InputState.Create) {
            createHouseholdInteractor.execute(viewState!!.text)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        view.loadingSubject.onNext(result.status == InteractorStatus.Loading)

                    }, { error ->

                    }).addTo(disposables)
        } else {

        }
    }

    override fun detach() {
        disposables.dispose()
    }

    private fun isValidName(name: CharSequence): Boolean {
        return nameValidator.validate(name)
    }

    private fun isValidMail(mail: CharSequence): Boolean {
        return emailValidator.validate(mail)
    }
}