package flhan.de.financemanager.ui.main.expenses.createedit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.base.scheduler.SchedulerProvider
import flhan.de.financemanager.common.CreateEditMode
import flhan.de.financemanager.common.CreateEditMode.Create
import flhan.de.financemanager.common.CreateEditMode.Edit
import flhan.de.financemanager.common.UserSettings
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.common.extensions.cleanUp
import flhan.de.financemanager.common.extensions.toListItem
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import java.text.DecimalFormat
import java.util.*

//TODO: Error Handling
class CreateEditExpenseViewModel
(
        findExpenseByIdInteractor: FindExpenseByIdInteractor,
        fetchUsersInteractor: FetchUsersInteractor,
        userSettings: UserSettings,
        @CreateEditExpenseModule.ExpenseId expenseId: String?,
        private val saveExpenseInteractor: CreateUpdateExpenseInteractor,
        private val scheduler: SchedulerProvider
) : ViewModel() {

    var amountString: String = ""
        set(value) {
            amount.value = onAmountChanged(value)
        }

    val isLoading = MutableLiveData<Boolean>()
    val hasError = MutableLiveData<Boolean>()
    val mode = MutableLiveData<CreateEditMode>()
    val cause = MutableLiveData<String>()
    val amount = MutableLiveData<String>()
    val userItems = MutableLiveData<List<CreateEditUserItem>>()
    val selectedUserIndex = MutableLiveData<Int>()

    private var expense: Expense? = null
    private val currentUserId: String
    private val disposables = CompositeDisposable()

    init {
        isLoading.value = false
        hasError.value = false
        currentUserId = userSettings.getUserId()

        val expenseObservable: Observable<InteractorResult<Expense>>

        val usersObservable = fetchUsersInteractor.fetchAll().subscribeOn(scheduler.io()).replay(1).refCount()
        if (!expenseId.isNullOrEmpty()) {
            mode.value = Edit
            expenseObservable = findExpenseByIdInteractor.findExpense(expenseId!!)
                    .subscribeOn(scheduler.io())
                    .replay(1).refCount()

        } else {
            mode.value = Create
            expenseObservable = Observable.just(InteractorResult(Success, Expense()))
                    .replay(1).refCount()
        }

        val loadingObservable: Observable<Boolean> = Observable.combineLatest(expenseObservable, usersObservable, BiFunction { expense, userStatus ->
            return@BiFunction expense.status == Loading || userStatus.status == Loading
        })

        loadingObservable
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.main())
                .subscribe { isLoading.value = it }.addTo(disposables)

        Observable.zip(
                expenseObservable.filter { it.status == Success },
                usersObservable.filter { it.status == Success },
                BiFunction { expenseResult: InteractorResult<Expense>, userResult: InteractorResult<MutableList<User>> ->
                    onExpenseLoaded(expenseResult.result!!)
                    onUsersLoaded(userResult.result!!)
                })
                .subscribeOn(scheduler.main())
                .subscribe { onLoadingFinished() }
                .addTo(disposables)
    }

    fun onUserSelected(position: Int) {
        if (selectedUserIndex.value != position) {
            selectedUserIndex.value = position
        }
    }

    fun onSaveClicked(success: () -> Unit) {
        expense!!.apply {
            cause = this@CreateEditExpenseViewModel.cause.value!!
            amount = this@CreateEditExpenseViewModel.amount.value!!.toDouble()
            createdAt = createdAt ?: Date()
            creator = userItems.value!![selectedUserIndex.value!!].id!!
        }

        saveExpenseInteractor.save(expense!!)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.main())
                .subscribe { result ->
                    isLoading.value = result.status == Loading
                    if (result.status == Success) {
                        success()
                    } else if (result.status == Error) {
                        handleError(result.exception!!)
                    }
                }
    }

    //TODO: Extract
    fun onAmountChanged(amountString: String?): String {
        val amountWithDecimalPlaces = when {
            amountString.isNullOrEmpty() -> "0.00"
            amountString!!.length == 1 -> "0.0" + amountString
            amountString.length == 2 -> "0." + amountString
            else -> {
                val beforeDecSep = amountString.substring(0, amountString.length - 2)
                val afterDecSeparator = amountString.substring(amountString.length - 2, amountString.length)
                String.format("%s.%s", beforeDecSep, afterDecSeparator)
            }
        }

        val format = DecimalFormat.getInstance() as DecimalFormat
        format.applyPattern("###,##0.00")
        val moneyAmount = format.format(amountWithDecimalPlaces.toDouble())
        return String.format("%s %s", moneyAmount, format.currency.symbol)
    }

    override fun onCleared() {
        disposables.cleanUp()
        super.onCleared()
    }

    private fun onLoadingFinished() {
        if (expense!!.creator.isNotBlank()) {
            selectUserById(expense!!.creator)
        } else {
            selectUserById(currentUserId)
        }
    }

    private fun handleError(exception: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun onExpenseLoaded(expense: Expense) {
        this.expense = expense
        amountString = expense.amount?.toString() ?: ""
        cause.value = expense.cause
    }

    private fun onUsersLoaded(users: MutableList<User>) {
        userItems.value = users.map { it.toListItem() }
    }

    private fun selectUserById(id: String) {
        val index = userItems.value?.indexOfFirst { it.id?.equals(id) ?: false }
        selectedUserIndex.value = index
    }
}