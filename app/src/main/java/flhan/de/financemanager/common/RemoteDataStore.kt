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
import io.reactivex.ObservableEmitter
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
    fun loadUsers(): Observable<MutableList<User>>
}

class FirebaseClient(
        val userSettings: UserSettings
) : RemoteDataStore {
    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val rootReference by lazy { firebaseDatabase.getReference("households") }
    private val usersObservable: Observable<MutableList<User>>

    init {
        usersObservable = loadUsers()
                .replay(1)
                .refCount()
    }

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
        return usersObservable.flatMap { users ->
            return@flatMap createExpenseObservable(users)
        }
    }

    override fun loadUsers(): Observable<MutableList<User>> {
        return Observable.create { emitter: ObservableEmitter<MutableList<User>> ->
            val users = mutableListOf<User>()
            var isInitialLoadingDone = false

            val listener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(p0?.toException()?.cause ?: Throwable("Listener.OnCancelled"))
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    isInitialLoadingDone = true
                    emitter.onNext(users)
                }
            }
            val childListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    if (p0 != null) {
                        val user = p0.getValue(User::class.java)
                        user?.id = p0.key
                        if (user != null)
                            users.add(user)
                    }
                    if (isInitialLoadingDone)
                        emitter.onNext(users)
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }

            }

            //TODO: Remove hardcoded value
            val ref = rootReference.child("-Kva_1jCpajfZiuLeqoD/users")
            ref.addListenerForSingleValueEvent(listener)
            ref.addChildEventListener(childListener)
            emitter.setCancellable {
                ref.removeEventListener(listener)
                ref.removeEventListener(childListener)
            }
        }
    }


    private fun createExpenseObservable(users: List<User>): Observable<RepositoryEvent<Expense>> {
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
                        expense?.user = users.first { expense?.creator == it.id }
                        val event = Update<Expense>(expense!!)
                        it.onNext(event)
                    }
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    if (p0 != null) {
                        val expense = p0.getValue(Expense::class.java)
                        expense?.id = p0.key
                        expense?.user = users.first { expense?.creator == it.id }
                        val event = Create<Expense>(expense!!)
                        it.onNext(event)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    if (p0 != null) {
                        val key = p0.key
                        val event = Delete<Expense>(key)
                        it.onNext(event)
                    }
                }
            }
            //TODO: Remove hardcoded value
            //            rootReference.child("${userSettings.getHouseholdId()}/expenses").addChildEventListener(listener)
            rootReference.child("-Kva_1jCpajfZiuLeqoD/expenses").addChildEventListener(listener)
            it.setCancellable { rootReference.removeEventListener(listener) }
        }
    }
}