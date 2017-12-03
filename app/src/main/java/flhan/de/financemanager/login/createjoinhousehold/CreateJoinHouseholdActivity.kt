package flhan.de.financemanager.login.createjoinhousehold

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.focusChanges
import com.jakewharton.rxbinding2.widget.textChangeEvents
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.stringByName
import flhan.de.financemanager.common.extensions.toast
import flhan.de.financemanager.common.extensions.visible
import flhan.de.financemanager.main.MainActivity
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_create_join_household.*
import javax.inject.Inject

class CreateJoinHouseholdActivity : BaseActivity(), CreateJoinHouseholdContract.View {
    override lateinit var clickSubject: Subject<Unit>
    override lateinit var stateObservable: Observable<ViewState>
    override lateinit var nameObservable: Observable<CharSequence>
    override lateinit var emailObservable: Observable<CharSequence>

    private var canSubmit = false

    @Inject
    lateinit var presenter: CreateJoinHouseholdContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_household)
        setSupportActionBar(toolbar)

        setupTextListeners()
        setupFocusListeners()

        stateObservable = emailObservable.map { ViewState(it.toString(), InputState.Join) }
                .mergeWith(nameObservable.map { ViewState(it.toString(), InputState.Create) })
                .share()

        clickSubject = PublishSubject.create()

        presenter.attach()

        presenter.canSubmitObservable.subscribe {
            this.canSubmit = it
            invalidateOptionsMenu()
        }.addTo(disposables)

        presenter.errorObservable.subscribe { error ->
            when (error.type) {
                ErrorType.NoSuchHousehold -> setEmailError(stringByName(error.message!!))
                ErrorType.Unknown -> toast(stringByName(stringByName(error.message!!)))
                ErrorType.None -> setEmailError(null)
            }
        }.addTo(disposables)

        presenter.loadingObservable.subscribe { setLoading(it) }.addTo(disposables)
    }

    private fun setEmailError(message: String?) {
        create_join_household_join_mail_text.error = message
    }

    override fun dismiss() {
        presenter.detach()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setLoading(showLoading: Boolean) {
        create_join_household_loading.visible(showLoading)
    }

    private fun setupTextListeners() {
        nameObservable = create_join_household_create_name_text.textChangeEvents().map { it.text() }
        emailObservable = create_join_household_join_mail_text.textChangeEvents().map { it.text() }
    }

    private fun setupFocusListeners() {
        create_join_household_create_name_text.focusChanges()
                .distinctUntilChanged()
                .subscribe { hasFocus ->
                    if (hasFocus) {
                        create_join_household_join_mail_text.setText("")
                    }
                }.addTo(disposables)

        create_join_household_join_mail_text.focusChanges()
                .distinctUntilChanged()
                .subscribe { hasFocus ->
                    if (hasFocus) {
                        create_join_household_create_name_text.setText("")
                    }
                }.addTo(disposables)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_join_household_menu, menu)
        menu?.findItem(R.id.create_join_household_action_done)?.isVisible = canSubmit
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.create_join_household_action_done) {
            clickSubject.onNext(Unit)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
