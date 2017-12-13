package flhan.de.financemanager.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.jakewharton.rxbinding2.view.clicks
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    companion object {
        const val SIGN_IN_ID: Int = 12515
    }

    private val mGoogleApiClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build()
    }
    private val mGoogleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()
    }

    private val loginButton: SignInButton by lazy { login_with_google }

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        setContentView(R.layout.activity_login)
        loginButton.clicks().subscribe { startGoogleAuth() }.addTo(disposables)
        viewModel.loginSuccess.observe(this, Observer { success ->
            if (success == true) {
                dismiss()
            }
        })

        viewModel.error.observe(this, Observer { error ->
            error?.let { showErrorDialog(it) }
        })
    }

    fun dismiss() {
        finish()
    }

    private fun startGoogleAuth() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, SIGN_IN_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_ID) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                result.signInAccount?.idToken?.let {
                    viewModel.startAuth(it)
                }
            } else {
                showErrorDialog()
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        showErrorDialog(R.string.error_connection_body, R.string.error_connection_title)
    }
}
