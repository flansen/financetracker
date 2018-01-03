package flhan.de.financemanager.ui.login.createjoinhousehold.create

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.start
import flhan.de.financemanager.common.extensions.stringByName
import flhan.de.financemanager.common.extensions.toast
import flhan.de.financemanager.common.extensions.visible
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinErrorState
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.*
import flhan.de.financemanager.ui.login.createjoinhousehold.join.JoinHouseholdActivity
import flhan.de.financemanager.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_create_join_household.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class CreateHouseholdActivity : BaseActivity() {

    @Inject
    lateinit var factory: CreateHouseholdViewModelFactory

    private lateinit var viewModel: CreateHouseholdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_household)
        ButterKnife.bind(this)
        viewModel = ViewModelProviders.of(this, factory).get(CreateHouseholdViewModel::class.java)
        setupView()
    }

    @OnClick(R.id.joinHousehold)
    fun onJoinClicked() {
        start(JoinHouseholdActivity::class)
    }

    @OnClick(R.id.createHousehold)
    fun onCreateClicked() {
        viewModel.submit { startOverview() }
    }

    @OnTextChanged(R.id.secretText, callback = AFTER_TEXT_CHANGED)
    fun onNameChanged(name: Editable) {
        viewModel.secret.value = name.toString()
    }

    @OnTextChanged(R.id.nameText, callback = AFTER_TEXT_CHANGED)
    fun onMailChanged(mail: Editable) {
        viewModel.name.value = mail.toString()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(R.string.create_household_title)
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
        viewModel.secret.observe(this, Observer {
            val text = secretText.text
            if (text.toString() != it) {
                secretText.setText(it)
            }
        })
    }

    private fun handleError(errorState: CreateJoinErrorState?) {
        errorState?.apply {
            when (errorState.type) {
                NoSuchHousehold -> toast(stringByName(errorState.message!!))
                Unknown -> toast(stringByName(errorState.message!!))
                None -> {
                }
            }
        }
    }

    private fun startOverview() {
        currentFocus.let {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        start(MainActivity::class)
        finish()
    }
}
