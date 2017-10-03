package flhan.de.financemanager.common

import com.google.firebase.database.FirebaseDatabase
import flhan.de.financemanager.data.Household
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

/**
 * Created by Florian on 14.09.2017.
 */
interface RemoteDataStore {
    fun init()
    fun createHousehold(household: Household): Single<Household>
}

class FirebaseClient @Inject constructor() : RemoteDataStore {
    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val rootReference by lazy { firebaseDatabase.getReference("households") }

    override fun init() {
        firebaseDatabase.setPersistenceEnabled(true)
    }

    override fun createHousehold(household: Household): Single<Household> {
        return Single.create<Household> { emitter: SingleEmitter<Household> ->
            try {
                val key = rootReference.push().key
                household.id = key
                rootReference.child(key).setValue(household)
                emitter.onSuccess(household)
            } catch (ex: Exception) {
                emitter.onError(ex)
            }
        }
    }
}

/*private fun testStore() {
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("")

    val emptyList = ArrayList<User>()

    val households: List<Household> = mutableListOf(
            Household("Household 1", "", emptyList),
            Household("Household 2", "", emptyList)
    )

    households.forEach {
        val key = reference.child("households").push().key
        it.id = key
        reference.child("households").child(key).setValue(it)
    }

    val user = User("Name", "test@web.de")
    households[0].users.add(user)
    val nkey = reference.child("households/${households[0].id}/users/").push().key
    user.id = nkey
    reference.child("households/${households[0].id}/users/").child(nkey).setValue(user)
}
*/