package flhan.de.financemanager.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API
import com.google.android.gms.auth.api.Auth.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.ui.login.createjoinhousehold.CreateJoinHouseholdActivity
import javax.inject.Inject

class LoginActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private const val SIGN_IN_ID: Int = 12515
    }

    private val mGoogleApiClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build()
    }
    private val mGoogleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()
    }

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        viewModel.error.observe(this, Observer { error ->
            error?.let { showErrorDialog(it) }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_ID) {
            val result = GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                result.signInAccount?.idToken?.let {
                    viewModel.startAuth(it, { startCreateJoin() })
                }
            } else {
                showErrorDialog()
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        showErrorDialog(R.string.error_connection_body, R.string.error_connection_title)
    }

    @OnClick(R.id.login_with_google)
    fun onLoginClicked() {
        startGoogleAuth()
    }

    private fun startCreateJoin() {
        viewModel.dispose()
        startActivity(Intent(this, CreateJoinHouseholdActivity::class.java))
        finish()
    }

    private fun startGoogleAuth() {
        val signInIntent = GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, SIGN_IN_ID)
    }
}
