package flhan.de.financemanager.common.datastore

import com.google.firebase.database.*
import flhan.de.financemanager.base.RequestResult
import flhan.de.financemanager.common.data.ShoppingItem
import flhan.de.financemanager.common.data.Tag
import flhan.de.financemanager.di.HouseholdId
import flhan.de.financemanager.di.UserId
import flhan.de.financemanager.ui.main.expenses.createedit.NoItemFoundThrowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import javax.inject.Inject

interface ShoppingItemDataStore {
    fun loadShoppingItems(): Observable<MutableList<ShoppingItem>>
    fun saveItem(item: ShoppingItem): Observable<Unit>
    fun findById(id: String): Observable<RequestResult<ShoppingItem>>
    fun removeItems(items: List<ShoppingItem>)
    fun incrementTagByName(tagString: String): Observable<Unit>
    val tags: Observable<MutableList<Tag>>
}

class ShoppingItemDataStoreImpl @Inject constructor(@HouseholdId private val householdId: String,
                                                    @UserId private val userId: String
) : ShoppingItemDataStore {

    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val rootReference by lazy { firebaseDatabase.getReference(HOUSEHOLD) }
    override val tags by lazy { loadTags().replay(1).refCount() }

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
                            val index = shoppingItems.indexOfFirst { id == it.id }
                            shoppingItems[index] = this
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

    private fun loadTags(): Observable<MutableList<Tag>> {
        return Observable.create { emitter: ObservableEmitter<MutableList<Tag>> ->
            val tags = mutableListOf<Tag>()
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
                            val tag = snapshot.getValue(Tag::class.java)
                            tag?.apply {
                                id = snapshot.key
                                tags.add(this)
                            }
                        }
                    }
                    emitter.onNext(tags)
                }
            }
            val childListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                    p0 ?: return
                    val tagFromRemote = p0.getValue(Tag::class.java) ?: return
                    tags.forEachIndexed { index, tag ->
                        if (tag.id == p0.key) {
                            tags[index].numberOfOccurence = tagFromRemote.numberOfOccurence
                            return
                        }
                    }
                }

                override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                    if (isInitialLoadingDone) {
                        dataSnapshot?.let {
                            val tag = dataSnapshot.getValue(Tag::class.java)
                            tag?.apply {
                                id = dataSnapshot.key
                                tags.add(this)
                            }
                        }
                        emitter.onNext(tags)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            }
            val ref = rootReference.child("$householdId/$TAG")
            ref.keepSynced(true)
            ref.addChildEventListener(childListener)
            ref.addListenerForSingleValueEvent(valueListener)
            emitter.setCancellable {
                ref.removeEventListener(valueListener)
                ref.removeEventListener(childListener)
            }
        }
    }

    override fun saveItem(item: ShoppingItem): Observable<Unit> {
        return if (item.id.isBlank()) {
            createItem(item)
        } else {
            updateItem(item)
        }
    }

    override fun findById(id: String): Observable<RequestResult<ShoppingItem>> {
        return Observable.create { emitter: ObservableEmitter<RequestResult<ShoppingItem>> ->
            val ref = rootReference.child("$householdId/$SHOPPING_ITEMS/$id")

            val listener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {
                    emitter.onNext(RequestResult(null, NoItemFoundThrowable("Could not find Expense for id $id")))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    dataSnapshot?.let {
                        val item = dataSnapshot.getValue(ShoppingItem::class.java)
                        if (item != null) {
                            emitter.onNext(RequestResult(item))
                        } else {
                            emitter.onNext(RequestResult(null, NoItemFoundThrowable("Could not find ShoppingItem for id $id")))
                        }
                    }
                    emitter.onComplete()
                }
            }
            ref.addListenerForSingleValueEvent(listener)
            emitter.setCancellable { ref.removeEventListener(listener) }
        }.onErrorReturn { RequestResult(null, NoItemFoundThrowable("Could not find ShoppingItem for id $id")) }
    }

    override fun incrementTagByName(tagString: String): Observable<Unit> {
        return tags.doOnNext {
            val matchingItems = it.filter { it.name.trim().toLowerCase() == tagString.toLowerCase() }
            if (matchingItems.isEmpty()) {
                val tag = Tag(tagString, numberOfOccurence = 1)
                val ref = rootReference.child("$householdId/$TAG/").push()
                tag.id = ref.key
                ref.setValue(tag)
            } else {
                val dbTag = matchingItems.first()
                val itemRef = rootReference.child("$householdId/$TAG/${dbTag.id}")
                val updateMap = mapOf<String, Any>(
                        Tag.COUNT to dbTag.numberOfOccurence + 1
                )
                itemRef.updateChildren(updateMap)
            }
        }.map { Unit }
    }

    override fun removeItems(items: List<ShoppingItem>) {
        items.forEach {
            val ref = rootReference.child("$householdId/$SHOPPING_ITEMS/${it.id}")
            ref.removeValue()
        }
    }

    private fun updateItem(item: ShoppingItem): Observable<Unit> {
        return Observable.create { emitter ->
            val itemRef = rootReference.child("$householdId/$SHOPPING_ITEMS/${item.id}")
            val updateMap = mapOf<String, Any?>(
                    ShoppingItem.NAME to item.name,
                    ShoppingItem.CHECKED to item.isChecked,
                    ShoppingItem.CHECKED_AT to item.checkedAt,
                    ShoppingItem.TAG to item.tag
            )
            itemRef.updateChildren(updateMap) { databaseError, _ ->
                if (databaseError == null) {
                    emitter.onNext(Unit)
                    emitter.onComplete()
                } else {
                    emitter.onError(databaseError.toException())
                }
            }
        }
    }

    private fun createItem(item: ShoppingItem): Observable<Unit> {
        return Observable.create { emitter ->
            val ref = rootReference.child("$householdId/$SHOPPING_ITEMS/").push()
            item.id = ref.key
            item.creatorId = userId
            ref.setValue(item)
            emitter.onNext(Unit)
            emitter.onComplete()
        }
    }

    companion object {
        const val SHOPPING_ITEMS = "shoppingitems"
        const val HOUSEHOLD = "households"
        const val TAG = "tags"
    }
}