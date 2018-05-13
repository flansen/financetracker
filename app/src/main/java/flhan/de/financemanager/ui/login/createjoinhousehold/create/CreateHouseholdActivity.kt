package flhan.de.financemanager.ui.login.createjoinhousehold.create

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.common.extensions.goUp
import flhan.de.financemanager.common.extensions.stringByName
import flhan.de.financemanager.common.extensions.toast
import flhan.de.financemanager.common.extensions.visible
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinErrorState
import flhan.de.financemanager.ui.login.createjoinhousehold.ErrorType.*
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            goUp()
            true
        } else {
            false
        }
    }

    @OnClick(R.id.createHousehold)
    fun onCreateClicked() {
        viewModel.submit { startOverview() }
    }

    @OnTextChanged(R.id.secretText, callback = AFTER_TEXT_CHANGED)
    fun onSecretChanged(secret: Editable) {
        viewModel.secret.value = secret.toString()
    }

    @OnTextChanged(R.id.nameText, callback = AFTER_TEXT_CHANGED)
    fun onNameChanged(name: Editable) {
        viewModel.name.value = name.toString()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.create_household_title)
        }
        viewModel.apply {
            createEnabled.observe(this@CreateHouseholdActivity, Observer {
                val isEnabled = it ?: false
                createHousehold.isEnabled = isEnabled
            })
            errorState.observe(this@CreateHouseholdActivity, Observer { handleError(it) })
            isLoading.observe(this@CreateHouseholdActivity, Observer {
                createJoinLoadingView.visible(it ?: false)
            })
            name.observe(this@CreateHouseholdActivity, Observer {
                val text = nameText.text
                if (text.toString() != it) {
                    nameText.setText(it)
                }
            })
            secret.observe(this@CreateHouseholdActivity, Observer {
                val text = secretText.text
                if (text.toString() != it) {
                    secretText.setText(it)
                }
            })
        }
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
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
