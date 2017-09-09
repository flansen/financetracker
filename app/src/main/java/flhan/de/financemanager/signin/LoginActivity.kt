package flhan.de.financemanager.signin

import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.SignInButton
import com.jakewharton.rxbinding2.view.clicks
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    private val loginButton: SignInButton
        get() = this.login_with_google

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.clicks().subscribe {
            Log.d("Login", "click")
        }
    }
}
