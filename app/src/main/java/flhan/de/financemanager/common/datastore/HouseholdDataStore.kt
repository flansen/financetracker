package flhan.de.financemanager.common.datastore

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import flhan.de.financemanager.base.RequestResult
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.ui.login.createjoinhousehold.join.InvalidSecretThrowable
import flhan.de.financemanager.ui.login.createjoinhousehold.join.NoSuchHouseholdThrowable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject


/**
 * Created by Florian on 14.09.2017.
 */
interface HouseholdDataStore {
    fun createHousehold(household: Household): Single<RequestResult<Household>>
    fun joinHousehold(household: Household): Single<RequestResult<Household>>
    fun joinHouseholdByMail(email: String, secret: String): Single<RequestResult<Household>>
}

class HouseholdDataStoreImpl @Inject constructor(private val userSettings: UserSettings) : HouseholdDataStore {

    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val rootReference by lazy { firebaseDatabase.getReference(HOUSEHOLD) }

    override fun createHousehold(household: Household): Single<RequestResult<Household>> {
        return Single.create { emitter: SingleEmitter<RequestResult<Household>> ->
            val key = rootReference.push().key
            household.apply {
                creator = userSettings.getCurrentUser().email
                id = key
            }
            rootReference.child(key).setValue(household)
            emitter.onSuccess(RequestResult(household))
        }.onErrorReturn { RequestResult(null, it) }
    }

    override fun joinHousehold(household: Household): Single<RequestResult<Household>> {
        return Single.create { emitter: SingleEmitter<RequestResult<Household>> ->
            performJoin(household, household.secret, {
                emitter.onSuccess(RequestResult(household))
            }, { throwable ->
                emitter.onSuccess(RequestResult(null, throwable))
            })
        }.onErrorReturn { RequestResult(null, it) }
    }


    override fun joinHouseholdByMail(email: String, secret: String): Single<RequestResult<Household>> {
        return Single.create({ emitter: SingleEmitter<RequestResult<Household>> ->
            rootReference.orderByChild(CREATOR)
                    .equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onCancelled(databaseError: DatabaseError?) {
                            emitter.onSuccess(RequestResult(null, databaseError?.toException()
                                    ?: Exception("Could not fetch household for email $email")))
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            if (dataSnapshot?.childrenCount?.toInt() != 0) {
                                val first = dataSnapshot?.children?.first()
                                val household = first?.getValue(Household::class.java)
                                performJoin(household!!, secret, {
                                    emitter.onSuccess(RequestResult(household))
                                }, { throwable ->
                                    emitter.onSuccess(RequestResult(null, throwable))
                                })
                            } else {
                                emitter.onSuccess(RequestResult(null, NoSuchHouseholdThrowable()))
                            }
                        }
                    })
        }).onErrorReturn { RequestResult(null, it) }
    }


    private fun performJoin(household: Household, secret: String, success: () -> Unit, error: (Throwable) -> Unit) {
        val user = userSettings.getCurrentUser()
        val householdRef = rootReference.child(household.id)

        householdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                error(Throwable("Cancelled"))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val householdToJoin = dataSnapshot?.getValue(Household::class.java)
                if (householdToJoin == null) {
                    error(NoSuchHouseholdThrowable())
                    return
                }

                if (householdToJoin.secret == secret) {
                    val householdUserRef = householdRef.child(USERS)
                    val userId = householdUserRef.push().key
                    user.id = userId
                    household.users.put(user.id, user)
                    householdUserRef.child(userId).setValue(user)
                    userSettings.apply {
                        setUserId(userId)
                        setHouseholdId(household.id)
                    }
                    success()
                } else {
                    error(InvalidSecretThrowable())
                }
            }
        })
    }

    companion object {
        const val USERS = "users"
        const val HOUSEHOLD = "households"
        const val CREATOR = "creator"
    }
}