package flhan.de.financemanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import flhan.de.financemanager.data.Household
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.GoogleAuthProvider


class OverviewActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private val SIGN_IN_ID = 123
    private val mGoogleApiClient : GoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
            .build()
    }
    private val mGoogleSignInOptions : GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()
    }
    private val mAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        signIn()

        testStore()
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, SIGN_IN_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_ID) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount
            acct.let {
                firebaseAuthWithGoogle(acct!!)
            }
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val token = acct.idToken
        val credential = GoogleAuthProvider.getCredential(token, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        println(user?.displayName)
                        testStore()
                    } else {
                        // If sign in fails, display a message to the user.
                    }
                })
    }

    private fun testStore() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("root")

        val households: List<Household> = mutableListOf(
                Household("Household 1"),
                Household("Household 2")
        )

        households.forEach {
            val key = reference.child("households").push().key
            it.id = key
            reference.child("households").child(key).setValue(it)
        }

    }
}
