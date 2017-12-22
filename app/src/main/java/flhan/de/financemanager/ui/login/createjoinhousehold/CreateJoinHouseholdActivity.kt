package flhan.de.financemanager.ui.login.createjoinhousehold

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnFocusChange
import butterknife.OnTextChanged
import butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.stringByName
import flhan.de.financemanager.common.extensions.toast
import flhan.de.financemanager.common.extensions.visible
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinFocusTarget.Email
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinFocusTarget.Name
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.*
import flhan.de.financemanager.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_create_join_household.*
import javax.inject.Inject


//TODO: Split join and create views
//TODO: Implement join "secreet"
class CreateJoinHouseholdActivity : BaseActivity() {

    @Inject
    lateinit var factory: CreateJoinHouseholdViewModelFactory

    private lateinit var viewModel: CreateJoinHouseholdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_household)
        ButterKnife.bind(this)
        viewModel = ViewModelProviders.of(this, factory).get(CreateJoinHouseholdViewModel::class.java)
        setupView()
    }

    @OnClick(R.id.joinHousehold)
    fun onJoinClicked(view: View) {
        viewModel.submit { startOverview() }
    }

    @OnClick(R.id.createHousehold)
    fun onCreateClicked(view: View) {
        viewModel.submit { startOverview() }
    }

    @OnTextChanged(R.id.nameText, callback = AFTER_TEXT_CHANGED)
    fun onNameChanged(name: Editable) {
        viewModel.name.value = name.toString()
    }

    @OnTextChanged(R.id.mailText, callback = AFTER_TEXT_CHANGED)
    fun onMailChanged(mail: Editable) {
        viewModel.mail.value = mail.toString()
    }

    @OnFocusChange(value = [R.id.mailText, R.id.nameText])
    fun onFocusChanged(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            when (view.id) {
                R.id.mailText -> viewModel.focusChanged(Email)
                R.id.nameText -> viewModel.focusChanged(Name)
            }
        }
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        viewModel.joinEnabled.observe(this, Observer {
            val isEnabled = it ?: false
            joinHousehold.isEnabled = isEnabled
        })
        viewModel.createEnabled.observe(this, Observer {
            val isEnabled = it ?: false
            createHousehold.isEnabled = isEnabled
        })
        viewModel.errorState.observe(this, Observer { handleError(it) })
        viewModel.isLoading.observe(this, Observer { createJoinLoadingView.visible(it ?: false) })
        viewModel.name.observe(this, Observer {
            val text = nameText.text
            if (text.toString() != it) {
                nameText.setText(it)
            }
        })
        viewModel.mail.observe(this, Observer {
            val text = mailText.text
            if (text.toString() != it) {
                mailText.setText(it)
            }
        })
    }

    private fun handleError(errorState: CreateJoinErrorState?) {
        errorState?.apply {
            when (errorState.type) {
                NoSuchHousehold -> setEmailError(stringByName(errorState.message!!))
                Unknown -> toast(stringByName(stringByName(errorState.message!!)))
                None -> setEmailError(null)
            }
        }
    }

    private fun setEmailError(message: String?) {
        mailText.error = message
    }

    private fun startOverview() {
        currentFocus.let {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}
