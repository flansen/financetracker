package flhan.de.financemanager.login.createjoinhousehold

import flhan.de.financemanager.base.BasePresenter
import flhan.de.financemanager.base.InteractorStatus
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.GENERIC_ERROR_KEY
import flhan.de.financemanager.common.NO_SUCH_HOUSEHOLD_KEY
import flhan.de.financemanager.common.validators.EmailValidator
import flhan.de.financemanager.common.validators.NameValidator
import flhan.de.financemanager.login.createjoinhousehold.InputState.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.withLatestFrom

/**
 * Created by Florian on 29.09.2017.
 */
class CreateJoinHouseholdPresenter(
        private val view: CreateJoinHouseholdContract.View,
        private val nameValidator: NameValidator,
        private val emailValidator: EmailValidator,
        private val createHouseholdInteractor: CreateHouseholdInteractor,
        private val joinHouseholdByMailInteractor: JoinHouseholdByMailInteractor,
        schedulerProvider: SchedulerProvider
) : BasePresenter(schedulerProvider), CreateJoinHouseholdContract.Presenter {

    override lateinit var canSubmitObservable: Observable<Boolean>
    override lateinit var loadingObservable: Observable<Boolean>
    override lateinit var errorObservable: Observable<CreateJoinErrorState>

    override fun attach() {
        canSubmitObservable = view.stateObservable.map {
            when (it.inputState) {
                Create -> return@map isValidName(it.text)
                Join -> return@map isValidMail(it.text)
                else -> return@map false
            }
        }

        val interactorState =
                view.clickSubject
                        .withLatestFrom(view.stateObservable, { _, viewState -> viewState })
                        .flatMap { state ->
                            if (state.inputState == Create) {
                                createHouseholdInteractor.execute(state.text)
                            } else {
                                joinHouseholdByMailInteractor.execute(state.text)
                            }
                        }
                        .share()

        loadingObservable = interactorState
                .map { state ->
                    return@map state.status == InteractorStatus.Loading
                }

        errorObservable = interactorState
                .filter { it.status == InteractorStatus.Error }
                .map { state ->
                    if (state.exception is NoSuchHouseholdThrowable)
                        return@map CreateJoinErrorState(ErrorType.NoSuchHousehold, NO_SUCH_HOUSEHOLD_KEY)
                    return@map CreateJoinErrorState(ErrorType.Unknown, GENERIC_ERROR_KEY)
                }
                .mergeWith(view.clickSubject
                        .map { CreateJoinErrorState(ErrorType.None) })

        interactorState
                .filter { it.status == InteractorStatus.Success }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.main())
                .subscribe({
                    if (it.result != null)
                        view.dismiss()
                }, { error ->
                    println(error)
                })
                .addTo(disposables)
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