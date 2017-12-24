package flhan.de.financemanager.ui.main.expenses.createedit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.*
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

class CreateEditExpenseViewModel(
        findExpenseByIdInteractor: FindExpenseByIdInteractor,
        fetchUsersInteractor: FetchUsersInteractor,
        private val userSettings: UserSettings,
        @CreateEditExpenseModule.ExpenseId expenseId: String?)
    : ViewModel() {

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
        val usersObservable = fetchUsersInteractor.fetchAll()

        if (!expenseId.isNullOrEmpty()) {
            mode.value = Edit
            expenseObservable = findExpenseByIdInteractor.findExpense(expenseId!!)
        } else {
            mode.value = Create
            expenseObservable = Observable.just(InteractorResult(Success, Expense()))
        }

        val loadingObservable: Observable<Boolean> = Observable.combineLatest(expenseObservable, usersObservable, BiFunction { expense, userStatus ->
            return@BiFunction expense.status == Loading && userStatus.status == Loading
        })

        loadingObservable.subscribe {
            isLoading.value = it
        }.addTo(disposables)

        expenseObservable.subscribe { result ->
            when (result.status) {
                Loading -> {
                    /* do nothing*/
                }
                Error -> {
                }
                Success -> onExpenseLoaded(result.result!!)
            }
        }.addTo(disposables)

        usersObservable.subscribe { result ->
            when (result.status) {
                Loading -> {
                }
                Error -> {
                }
                Success -> onUsersLoaded(result.result!!)
            }
        }.addTo(disposables)

        Observable.combineLatest(
                expenseObservable.filter { it.status == Success },
                usersObservable.filter { it.status == Success },
                BiFunction { _: InteractorResult<Expense>, _: InteractorResult<MutableList<User>> ->
                    onLoadingFinished()
                })
                .subscribe { onLoadingFinished() }
                .addTo(disposables)
    }

    fun onUserSelected(position: Int) {
        if (selectedUserIndex.value != position) {
            selectedUserIndex.value = position
        }

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
        amount.value = expense.amount.toString()
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