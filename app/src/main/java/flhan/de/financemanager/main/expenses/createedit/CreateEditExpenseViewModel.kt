package flhan.de.financemanager.main.expenses.createedit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import flhan.de.financemanager.base.InteractorStatus.*
import flhan.de.financemanager.common.data.Expense
import io.reactivex.Observable

class CreateEditExpenseViewModel(
        findExpenseByIdInteractor: FindExpenseByIdInteractor,
        @CreateEditExpenseModule.ExpenseId expenseId: String)
    : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val hasError = MutableLiveData<Boolean>()

    init {
        isLoading.value = false
        val expenseObservable: Observable<Expense>
        if (!expenseId.isNullOrEmpty()) {
            expenseObservable = findExpenseByIdInteractor
                    .findExpense(expenseId)
                    .doOnNext { isLoading.value = it.status == Loading }
                    .doOnNext {
                        if (it.status == Error) {
                            handleError(it.exception)
                        }
                    }
                    .filter { it.status == Success }
                    .map { it.result!! }
        } else {
            expenseObservable = Observable.just(Expense())
        }

        expenseObservable.subscribe({ expense ->
            onExpenseLoaded(expense)
        }, { throwable ->

        })


    }

    private fun handleError(exception: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun onExpenseLoaded(expense: Expense) {
    }
}