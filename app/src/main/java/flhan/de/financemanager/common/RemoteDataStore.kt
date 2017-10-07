package flhan.de.financemanager.common

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import flhan.de.financemanager.base.RequestResult
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.data.Household
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.common.events.Create
import flhan.de.financemanager.common.events.Delete
import flhan.de.financemanager.common.events.RepositoryEvent
import flhan.de.financemanager.common.events.Update
import flhan.de.financemanager.login.createjoinhousehold.NoSuchHouseholdThrowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter

/**
 * Created by Florian on 14.09.2017.
 */
interface RemoteDataStore {
    fun init()
    fun createHousehold(household: Household): Single<RequestResult<Household>>
    fun joinHousehold(household: Household): Single<RequestResult<Household>>
    fun joinHouseholdByMail(email: String): Single<RequestResult<Household>>
    fun getCurrentUser(): User
    fun loadExpenses(): Observable<RepositoryEvent<Expense>>
}

class FirebaseClient(
        val userSettings: UserSettings
) : RemoteDataStore {
    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val rootReference by lazy { firebaseDatabase.getReference("households") }

    override fun init() {
        firebaseDatabase.setPersistenceEnabled(true)
    }

    override fun createHousehold(household: Household): Single<RequestResult<Household>> {
        return Single.create { emitter: SingleEmitter<RequestResult<Household>> ->
            household.creator = getCurrentUser().email
            val key = rootReference.push().key
            household.id = key
            rootReference.child(key).setValue(household)
            emitter.onSuccess(RequestResult(household))
        }.onErrorReturn { RequestResult(null, it) }
    }

    override fun joinHousehold(household: Household): Single<RequestResult<Household>> {
        return Single.create { emitter: SingleEmitter<RequestResult<Household>> ->
            performJoin(household)
            emitter.onSuccess(RequestResult(household))
        }.onErrorReturn { RequestResult(null, it) }
    }

    private fun performJoin(household: Household) {
        val user = getCurrentUser()

        val householdUserRef = rootReference.child("${household.id}/users/")
        val userId = householdUserRef.push().key
        user.id = userId
        household.users.put(user.id, user)
        householdUserRef.child(userId).setValue(user)
        userSettings.setUserId(userId)
        userSettings.setHouseholdId(household.id)
    }

    override fun getCurrentUser(): User {
        val user = User()
        val currentAuthorizedUser = FirebaseAuth.getInstance().currentUser
        if (currentAuthorizedUser != null) {
            user.name = currentAuthorizedUser.displayName ?: ""
            user.email = currentAuthorizedUser.email ?: ""
        }
        val userId = userSettings.getUserId()
        if (!userId.isEmpty()) {
            user.id = userId
        }
        return user
    }

    override fun joinHouseholdByMail(email: String): Single<RequestResult<Household>> {
        return Single.create({ emitter: SingleEmitter<RequestResult<Household>> ->
            rootReference.orderByChild("creator")
                    .equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError?) {
                            emitter.onSuccess(RequestResult(null, databaseError?.toException() ?: Exception("Could not fetch household for email $email")))
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            if (dataSnapshot?.childrenCount?.toInt() != 0) {
                                val first = dataSnapshot?.children?.first()
                                val household = first?.getValue(Household::class.java)
                                performJoin(household!!)
                                emitter.onSuccess(RequestResult(household))
                            } else {
                                emitter.onSuccess(RequestResult(null, NoSuchHouseholdThrowable()))
                            }
                        }
                    })
        }).onErrorReturn { RequestResult(null, it) }
    }

    override fun loadExpenses(): Observable<RepositoryEvent<Expense>> {
        return Observable.create {
            val listener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    it.onError(p0?.toException()?.cause ?: Throwable("Listener.OnCancelled"))
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                    if (p0 != null) {
                        val expense = p0.getValue(Expense::class.java)
                        expense?.id = p0.key
                        //TODO: Handle null
                        val event = Update<Expense>(expense!!)
                        it.onNext(event)
                    }
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    if (p0 != null) {
                        val expense = p0.getValue(Expense::class.java)
                        expense?.id = p0.key
                        //TODO: Handle null
                        val event = Create<Expense>(expense!!)
                        it.onNext(event)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    if (p0 != null) {
                        val key = p0.key
                        //TODO: Handle null
                        val event = Delete<Expense>(key)
                        it.onNext(event)
                    }
                }
            }
            rootReference.child(userSettings.getHouseholdId()).addChildEventListener(listener)
            it.setCancellable { rootReference.removeEventListener(listener) }
        }

    }
}