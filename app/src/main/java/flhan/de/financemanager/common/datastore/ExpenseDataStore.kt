package flhan.de.financemanager.common.datastore

import com.google.firebase.database.*
import flhan.de.financemanager.base.RequestResult
import flhan.de.financemanager.common.data.Billing
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.di.HouseholdId
import flhan.de.financemanager.ui.main.expenses.createedit.NoItemFoundThrowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.rxkotlin.withLatestFrom
import javax.inject.Inject

interface ExpenseDataStore {
    fun deleteExpenses(): Single<Boolean>
    fun loadExpenses(): Observable<MutableList<Expense>>
    fun findExpenseBy(id: String): Observable<RequestResult<Expense>>
    fun saveExpense(expense: Expense): Observable<Unit>
    fun deleteExpense(id: String): Single<Boolean>
    fun saveBilling(billing: Billing): Single<RequestResult<Billing>>
}

class ExpenseDataStoreImpl @Inject constructor(
        @HouseholdId private val householdId: String,
        private val usersObservable: Observable<MutableList<User>>
) : ExpenseDataStore {

    private val firebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val rootReference by lazy { firebaseDatabase.getReference(HOUSEHOLD) }

    override fun findExpenseBy(id: String): Observable<RequestResult<Expense>> {
        return usersObservable.flatMap { users ->
            Observable.create { emitter: ObservableEmitter<RequestResult<Expense>> ->
                val ref = rootReference.child("$householdId/$EXPENSES/$id")
                val listener = object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError?) {
                        emitter.onNext(RequestResult(null, NoItemFoundThrowable("Could not find Expense for id $id")))
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        dataSnapshot?.let {
                            val expense = dataSnapshot.getValue(Expense::class.java)
                            if (expense != null) {
                                expense.user = users.firstOrNull { expense.creator == it.id }
                                emitter.onNext(RequestResult(expense))
                            } else {
                                emitter.onNext(RequestResult(null, NoItemFoundThrowable("Could not find Expense for id $id")))
                            }
                        }
                        emitter.onComplete()
                    }
                }
                ref.addListenerForSingleValueEvent(listener)
                emitter.setCancellable { ref.removeEventListener(listener) }
            }
        }.onErrorReturn { RequestResult(null, NoItemFoundThrowable("Could not find Expense for id $id")) }
    }

    override fun saveExpense(expense: Expense): Observable<Unit> {
        return if (expense.id.isBlank()) {
            createExpense(expense)
        } else {
            updateExpense(expense)
        }
    }

    override fun deleteExpenses(): Single<Boolean> {
        return Single.create { emitter ->
            val ref = rootReference.child("$householdId/$EXPENSES/")
            ref.removeValue { error, ref ->
                if (error == null) {
                    emitter.onSuccess(true)
                } else {
                    emitter.onSuccess(false)
                }
            }

        }
    }

    override fun loadExpenses(): Observable<MutableList<Expense>> {
        return createExpenseObservable()
                .withLatestFrom(usersObservable, { expenses: MutableList<Expense>, userList: MutableList<User>? ->
                    for (expense in expenses.filter { it.user == null }) {
                        val user = userList?.firstOrNull { expense.creator == it.id }
                        expense.user = user
                    }
                    return@withLatestFrom expenses
                })
    }

    override fun deleteExpense(id: String): Single<Boolean> {
        return Single.fromCallable {
            val ref = rootReference.child("$householdId/$EXPENSES/$id")
            ref.removeValue()
            return@fromCallable true
        }
    }

    override fun saveBilling(billing: Billing): Single<RequestResult<Billing>> {
        return Single.create { emitter ->
            val billingRef = rootReference.child("$householdId/$BILLING/").push()
            billing.id = billingRef.key
            billingRef.setValue(billing, { error, ref ->
                if (error != null) {
                    emitter.onSuccess(RequestResult(null, error.toException()))
                } else {
                    emitter.onSuccess(RequestResult(billing))
                }
            })
        }
    }

    private fun updateExpense(expense: Expense): Observable<Unit> {
        return Observable.create { emitter ->
            val expenseRef = rootReference.child("$householdId/$EXPENSES/${expense.id}")
            val updateMap = mutableMapOf<String, Any>()
            updateMap[Expense.AMOUNT] = expense.amount!!
            updateMap[Expense.CAUSE] = expense.cause
            updateMap[Expense.CREATOR] = expense.creator
            updateMap[Expense.CREATOR_NAME] = expense.creatorName

            expenseRef.updateChildren(updateMap) { databaseError, _ ->
                if (databaseError == null) {
                    emitter.onNext(Unit)
                    emitter.onComplete()
                } else {
                    emitter.onError(databaseError.toException())
                }
            }
        }
    }

    private fun createExpense(expense: Expense): Observable<Unit> {
        return Observable.create { emitter ->
            val ref = rootReference.child("$householdId/$EXPENSES/").push()
            expense.id = ref.key
            ref.setValue(expense)
            emitter.onNext(Unit)
            emitter.onComplete()
        }
    }

    private fun createExpenseObservable(): Observable<MutableList<Expense>> {
        return Observable.create { emitter ->
            val expenses = mutableListOf<Expense>()
            var isInitialLoadingDone = false

            val valueListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {
                    emitter.onError(databaseError?.toException()
                            ?: Throwable("Listener.OnCancelled"))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    dataSnapshot?.apply {
                        for (snapshot in children) {
                            val expense = snapshot.getValue(Expense::class.java)
                            expense?.let {
                                expense.id = snapshot.key
                                expenses.add(expense)
                            }
                        }
                        isInitialLoadingDone = true
                        emitter.onNext(expenses)
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
                    if (isInitialLoadingDone) {
                        dataSnapshot?.let {
                            val expense = dataSnapshot.getValue(Expense::class.java)
                            expense?.apply {
                                id = dataSnapshot.key
                                val index = expenses.indexOfFirst { id == it.id }
                                expenses[index] = this
                            }
                            emitter.onNext(expenses)
                        }
                    }
                }

                override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
                    if (isInitialLoadingDone) {
                        dataSnapshot?.apply {
                            val expense = getValue(Expense::class.java)
                            expense?.apply {
                                id = key
                                expenses.add(this)
                            }
                            emitter.onNext(expenses)
                        }
                    }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                    dataSnapshot?.apply {
                        val key = key
                        val index = expenses.indexOfFirst { key == it.id }
                        expenses.removeAt(index)
                        emitter.onNext(expenses)
                    }
                }
            }

            val ref = rootReference.child("$householdId/$EXPENSES")
            ref.apply {
                keepSynced(true)
                addChildEventListener(childEventListener)
                addListenerForSingleValueEvent(valueListener)
            }
            emitter.setCancellable { rootReference.removeEventListener(childEventListener) }
        }
    }

    companion object {
        const val EXPENSES = "expenses"
        const val BILLING = "billing"
        const val HOUSEHOLD = "households"
    }

}