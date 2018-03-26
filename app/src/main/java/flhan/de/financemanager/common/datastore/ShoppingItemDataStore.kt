package flhan.de.financemanager.common.datastore

import com.google.firebase.database.*
import flhan.de.financemanager.common.data.ShoppingItem
import flhan.de.financemanager.di.HouseholdId
import io.reactivex.Observable
import javax.inject.Inject

interface ShoppingItemDataStore {
    fun loadShoppingItems(): Observable<MutableList<ShoppingItem>>
}

class ShoppingItemDataStoreImpl @Inject constructor(@HouseholdId private val householdId: String) : ShoppingItemDataStore {

    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val rootReference by lazy { firebaseDatabase.getReference(HOUSEHOLD) }

    override fun loadShoppingItems(): Observable<MutableList<ShoppingItem>> {
        return Observable.create { emitter ->
            val shoppingItems = mutableListOf<ShoppingItem>()
            var isInitialLoadingDone = false

            val valueListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {
                    emitter.onError(databaseError?.toException()
                            ?: Throwable("Listener.OnCancelled"))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    isInitialLoadingDone = true
                    dataSnapshot?.apply {
                        for (snapshot in children) {
                            val shoppingItem = snapshot.getValue(ShoppingItem::class.java)
                            shoppingItem?.apply {
                                id = snapshot.key
                                shoppingItems.add(this)
                            }
                        }
                        emitter.onNext(shoppingItems)
                    }
                }
            }

            val childEventListener = object : ChildEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {
                    emitter.onError(databaseError?.toException()?.cause
                            ?: Throwable("Listener.OnCancelled"))
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
                    dataSnapshot?.let {
                        val shoppingItem = dataSnapshot.getValue(ShoppingItem::class.java)
                        shoppingItem?.apply {
                            id = dataSnapshot.key
                            shoppingItems.add(this)
                        }
                        emitter.onNext(shoppingItems)
                    }

                }

                override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                    if (isInitialLoadingDone) {
                        dataSnapshot?.apply {
                            val shoppingItem = dataSnapshot.getValue(ShoppingItem::class.java)
                            shoppingItem?.apply {
                                id = dataSnapshot.key
                                shoppingItems.add(this)
                            }
                            emitter.onNext(shoppingItems)
                        }
                    }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                    dataSnapshot?.apply {
                        val key = key
                        val index = shoppingItems.indexOfFirst { key == it.id }
                        shoppingItems.removeAt(index)
                        emitter.onNext(shoppingItems)
                    }
                }
            }

            val ref = rootReference.child("$householdId/$SHOPPING_ITEMS")
            ref.apply {
                keepSynced(true)
                addChildEventListener(childEventListener)
                addListenerForSingleValueEvent(valueListener)
            }
            emitter.setCancellable { rootReference.removeEventListener(childEventListener) }
        }
    }

    companion object {
        const val SHOPPING_ITEMS = "shoppingitems"
        const val HOUSEHOLD = "households"
    }
}