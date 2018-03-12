package flhan.de.financemanager.ui.login.createjoinhousehold.join

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.*
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinErrorState
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.*
import flhan.de.financemanager.ui.login.createjoinhousehold.create.CreateHouseholdActivity
import flhan.de.financemanager.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_join_household.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class JoinHouseholdActivity : BaseActivity() {

    @Inject
    lateinit var factory: JoinHouseholdViewModelFactory

    private lateinit var viewModel: JoinHouseholdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_household)
        ButterKnife.bind(this)
        viewModel = ViewModelProviders.of(this, factory).get(JoinHouseholdViewModel::class.java)
        setupView()
    }

    @OnTextChanged(R.id.secretText, callback = AFTER_TEXT_CHANGED)
    fun onNameChanged(name: Editable) {
        viewModel.secret.value = name.toString()
    }

    @OnTextChanged(R.id.mailText, callback = AFTER_TEXT_CHANGED)
    fun onMailChanged(mail: Editable) {
        viewModel.mail.value = mail.toString()
    }

    @OnClick(R.id.joinHousehold)
    fun onCreateClicked() {
        viewModel.submit { startOverview() }
    }

    @OnClick(R.id.createHousehold)
    fun onJoinClicked() {
        start(CreateHouseholdActivity::class)
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.join_household)
        viewModel.apply {
            joinEnabled.observe(this@JoinHouseholdActivity, Observer {
                val isEnabled = it ?: false
                joinHousehold.isEnabled = isEnabled
            })
            errorState.observe(this@JoinHouseholdActivity, Observer { handleError(it) })
            isLoading.observe(this@JoinHouseholdActivity, Observer {
                joinLoadingView.visible(it ?: false)
            })
            mail.observe(this@JoinHouseholdActivity, Observer {
                val text = mailText.text
                if (text.toString() != it) {
                    mailText.setText(it)
                }
            })
            secret.observe(this@JoinHouseholdActivity, Observer {
                val text = secretText.text
                if (text.toString() != it) {
                    secretText.setText(it)
                }
            })
        }
    }

    private fun handleError(errorState: CreateJoinErrorState?) {
        when (errorState?.type) {
            NoSuchHousehold -> toast(stringByName(errorState.message!!))
            Unknown -> toast(stringByName(errorState.message!!))
            InvalidSecret -> toast(stringByName(errorState.message!!))
            None -> {
            }
        }
    }

    private fun startOverview() {
        hideKeyboard()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}