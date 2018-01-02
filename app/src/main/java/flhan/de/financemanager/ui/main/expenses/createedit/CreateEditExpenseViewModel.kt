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
import flhan.de.financemanager.common.util.CurrencyString
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import java.util.*

class CreateEditExpenseViewModel
(
        private val saveExpenseInteractor: CreateUpdateExpenseInteractor,
        private val scheduler: SchedulerProvider,
        findExpenseByIdInteractor: FindExpenseByIdInteractor,
        fetchUsersInteractor: FetchUsersInteractor,
        userSettings: UserSettings,
        @CreateEditExpenseModule.ExpenseId expenseId: String?
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val hasError = MutableLiveData<Boolean>()
    val mode = MutableLiveData<CreateEditMode>()
    val cause = MutableLiveData<String>()
    val amount = MutableLiveData<CurrencyString>()
    val userItems = MutableLiveData<List<CreateEditUserItem>>()
    val selectedUserIndex = MutableLiveData<Int>()

    private var expense: Expense? = null
    private val currentUserId: String
    private val disposables = CompositeDisposable()

    init {
        val expenseObservable: Observable<InteractorResult<Expense>>
        val usersObservable = fetchUsersInteractor.fetchAll()
                .subscribeOn(scheduler.io())
                .replay(1)
                .refCount()

        if (!expenseId.isNullOrEmpty()) {
            mode.value = Edit
            expenseObservable = findExpenseByIdInteractor
                    .findExpense(expenseId!!)
        } else {
            mode.value = Create
            expenseObservable = Observable
                    .just(InteractorResult(Success, Expense()))
        }

        expenseObservable
                .subscribeOn(scheduler.io())
                .replay(1)
                .refCount()

        val loadingObservable: Observable<Boolean> =
                Observable.combineLatest(
                        expenseObservable,
                        usersObservable,
                        BiFunction { expense, userStatus ->
                            return@BiFunction expense.status == Loading || userStatus.status == Loading
                        }
                )

        loadingObservable
                .observeOn(scheduler.main())
                .subscribe { isLoading.value = it }
                .addTo(disposables)

        Observable.zip(
                expenseObservable.observeOn(scheduler.main()).filter { it.status == Success },
                usersObservable.observeOn(scheduler.main()).filter { it.status == Success },
                BiFunction { expenseResult: InteractorResult<Expense>, userResult: InteractorResult<MutableList<User>> ->
                    onExpenseLoaded(expenseResult.result!!)
                    onUsersLoaded(userResult.result!!)
                })
                .observeOn(scheduler.main())
                .subscribe { onLoadingFinished() }
                .addTo(disposables)

        isLoading.value = false
        hasError.value = false
        currentUserId = userSettings.getUserId()
    }

    fun onUserSelected(position: Int) {
        if (selectedUserIndex.value != position) {
            selectedUserIndex.value = position
        }
    }

    fun onSaveClicked(success: () -> Unit) {
        expense!!.apply {
            cause = this@CreateEditExpenseViewModel.cause.value!!
            amount = this@CreateEditExpenseViewModel.amount.value!!.amount
            createdAt = createdAt ?: Date()
            creator = userItems.value!![selectedUserIndex.value!!].id!!
        }

        saveExpenseInteractor
                .save(expense!!)
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

    fun onAmountChanged(amountString: String) {
        amount.value = CurrencyString(amountString)
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
        amount.value = CurrencyString(expense.amount)
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