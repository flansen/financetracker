package flhan.de.financemanager.login.createjoinhousehold

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.ButterKnife
import butterknife.OnFocusChange
import butterknife.OnTextChanged
import butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.stringByName
import flhan.de.financemanager.common.extensions.toast
import flhan.de.financemanager.common.extensions.visible
import flhan.de.financemanager.login.LoginViewModelFactory
import flhan.de.financemanager.login.createjoinhousehold.CreateJoinHouseholdViewModel.CreateJoinFocusTarget.Create
import flhan.de.financemanager.login.createjoinhousehold.CreateJoinHouseholdViewModel.CreateJoinFocusTarget.Join
import flhan.de.financemanager.login.createjoinhousehold.ErrorType.*
import flhan.de.financemanager.main.MainActivity
import kotlinx.android.synthetic.main.activity_create_join_household.*
import javax.inject.Inject

class CreateJoinHouseholdActivity : BaseActivity() {

    @Inject
    lateinit var factory: LoginViewModelFactory

    lateinit var viewModel: CreateJoinHouseholdViewModel

    private var canSubmit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_household)
        ButterKnife.bind(this)
        viewModel = ViewModelProviders.of(this, factory).get(CreateJoinHouseholdViewModel::class.java)
        setSupportActionBar(toolbar)

        viewModel.canSubmit.observe(this, Observer { invalidateOptionsMenu() })
        viewModel.errorState.observe(this, Observer { handleError(it) })
        viewModel.isLoading.observe(this, Observer { create_join_household_loading.visible(it ?: false) })
        viewModel.name.observe(this, Observer {
            val text = create_join_household_create_name_text.text
            if (text.toString() != it) {
                create_join_household_create_name_text.setText(it)
            }
        })
        viewModel.mail.observe(this, Observer {
            val text = create_join_household_join_mail_text.text
            if (text.toString() != it) {
                create_join_household_join_mail_text.setText(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_join_household_menu, menu)
        menu?.findItem(R.id.create_join_household_action_done)?.isVisible = viewModel.canSubmit.value ?: false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.create_join_household_action_done) {
            viewModel.submit { startOverview() }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @OnTextChanged(R.id.create_join_household_create_name_text, callback = AFTER_TEXT_CHANGED)
    fun onNameChanged(name: Editable) {
        viewModel.name.value = name.toString()
    }

    @OnTextChanged(R.id.create_join_household_join_mail_text, callback = AFTER_TEXT_CHANGED)
    fun onMailChanged(mail: Editable) {
        viewModel.mail.value = mail.toString()
    }

    @OnFocusChange(value = [R.id.create_join_household_join_mail_text, R.id.create_join_household_create_name_text])
    fun onFocusChanged(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            when (view.id) {
                R.id.create_join_household_join_mail_text -> viewModel.focusChanged(Join)
                R.id.create_join_household_create_name_text -> viewModel.focusChanged(Create)
            }
        }
    }

    private fun handleError(errorState: CreateJoinErrorState?) {
        when (errorState?.type) {
            NoSuchHousehold -> setEmailError(stringByName(errorState.message!!))
            Unknown -> toast(stringByName(stringByName(errorState.message!!)))
            None -> setEmailError(null)
        }
    }

    private fun setEmailError(message: String?) {
        create_join_household_join_mail_text.error = message
    }

    private fun startOverview() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
