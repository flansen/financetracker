package flhan.de.financemanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import flhan.de.financemanager.data.Household


class OverviewActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private val SIGN_IN_ID = 123
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        testStore()
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
