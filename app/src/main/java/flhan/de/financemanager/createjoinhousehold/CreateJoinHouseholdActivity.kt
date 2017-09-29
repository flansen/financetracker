package flhan.de.financemanager.createjoinhousehold

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.widget.textChangeEvents
import flhan.de.financemanager.R
import flhan.de.financemanager.base.app
import flhan.de.financemanager.di.createjoinhousehold.CreateJoinHouseholdModule
import flhan.de.financemanager.extensions.toast
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_create_join_household.*
import javax.inject.Inject

class CreateJoinHouseholdActivity : AppCompatActivity(), CreateJoinHouseholdContract.View {
    override lateinit var nameObservable: Observable<CharSequence>
    override lateinit var emailObservable: Observable<CharSequence>

    private var canSubmit = false

    private val component by lazy { app.appComponent.plus(CreateJoinHouseholdModule(this)) }

    @Inject
    lateinit var presenter: CreateJoinHouseholdContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_household)
        setSupportActionBar(toolbar)
        component.inject(this)

        nameObservable = create_join_household_create_name_text.textChangeEvents().map { ev -> ev.text() }
        emailObservable = create_join_household_join_mail_text.textChangeEvents().map { ev -> ev.text() }

        presenter.attach()

        presenter.canSubmitObservable.subscribe { canSubmit ->
            this.canSubmit = canSubmit
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_join_household_menu, menu)
        menu?.findItem(R.id.create_join_household_action_done)?.isVisible = canSubmit
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.create_join_household_action_done) {
            toast("Done")
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
