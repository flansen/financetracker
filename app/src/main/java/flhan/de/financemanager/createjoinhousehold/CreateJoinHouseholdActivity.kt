package flhan.de.financemanager.createjoinhousehold

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.focusChanges
import com.jakewharton.rxbinding2.widget.textChangeEvents
import flhan.de.financemanager.R
import flhan.de.financemanager.base.app
import flhan.de.financemanager.di.createjoinhousehold.CreateJoinHouseholdModule
import flhan.de.financemanager.extensions.visible
import flhan.de.financemanager.overview.OverviewActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_create_join_household.*
import javax.inject.Inject

class CreateJoinHouseholdActivity : AppCompatActivity(), CreateJoinHouseholdContract.View {
    override lateinit var stateObservable: Observable<ViewState>
    override lateinit var nameObservable: Observable<CharSequence>
    override lateinit var emailObservable: Observable<CharSequence>
    override lateinit var loadingSubject: Subject<Boolean>

    private var canSubmit = false
    private val disposables = CompositeDisposable()
    private val component by lazy { app.appComponent.plus(CreateJoinHouseholdModule(this)) }

    @Inject
    lateinit var presenter: CreateJoinHouseholdContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_household)
        setSupportActionBar(toolbar)
        component.inject(this)

        setupTextListeners()
        setupFocusListeners()

        stateObservable = emailObservable.map { ViewState(it.toString(), InputState.Join) }
                .mergeWith(nameObservable.map { ViewState(it.toString(), InputState.Create) })

        loadingSubject = PublishSubject.create()
        loadingSubject.subscribe { setLoading(it) }.addTo(disposables)

        presenter.attach()

        presenter.canSubmitObservable.subscribe {
            this.canSubmit = it
            invalidateOptionsMenu()
        }.addTo(disposables)
    }

    override fun dismiss() {
        presenter.detach()
        startActivity(Intent(this, OverviewActivity::class.java))
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

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_join_household_menu, menu)
        menu?.findItem(R.id.create_join_household_action_done)?.isVisible = canSubmit
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.create_join_household_action_done) {
            presenter.onDoneClick()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
