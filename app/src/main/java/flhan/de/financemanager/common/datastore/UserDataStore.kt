package flhan.de.financemanager.common.datastore

import com.google.firebase.database.*
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.di.HouseholdId
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import javax.inject.Inject

interface UserDataStore {
    fun loadUsers(): Observable<MutableList<User>>
}

class UserDataStoreImpl @Inject constructor(
        @HouseholdId private val householdId: String
) : UserDataStore {

    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val rootReference by lazy { firebaseDatabase.getReference(HOUSEHOLD) }
    private val usersObservable by lazy {
        createUserObservable()
                .replay(1)
                .refCount()
    }

    override fun loadUsers(): Observable<MutableList<User>> {
        return usersObservable
    }

    private fun createUserObservable(): Observable<MutableList<User>> {
        return Observable.create { emitter: ObservableEmitter<MutableList<User>> ->
            val users = mutableListOf<User>()
            var isInitialLoadingDone = false

            val valueListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {
                    emitter.onError(databaseError?.toException()
                            ?: Throwable("Listener.OnCancelled"))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    isInitialLoadingDone = true
                    dataSnapshot?.let {
                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(User::class.java)
                            user?.let {
                                user.id = snapshot.key
                                users.add(user)
                            }
                        }
                    }
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

                override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                    if (isInitialLoadingDone) {
                        dataSnapshot?.let {
                            val user = dataSnapshot.getValue(User::class.java)
                            user?.let {
                                user.id = dataSnapshot.key
                                users.add(user)
                            }
                        }
                        emitter.onNext(users)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            }
            val ref = rootReference.child("$householdId/$USERS")
            ref.keepSynced(true)
            ref.addChildEventListener(childListener)
            ref.addListenerForSingleValueEvent(valueListener)
            emitter.setCancellable {
                ref.removeEventListener(valueListener)
                ref.removeEventListener(childListener)
            }
        }
    }

    companion object {
        const val USERS = "users"
        const val HOUSEHOLD = "households"
    }

}