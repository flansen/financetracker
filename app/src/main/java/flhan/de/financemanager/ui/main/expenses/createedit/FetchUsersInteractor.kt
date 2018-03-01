package flhan.de.financemanager.ui.main.expenses.createedit

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Loading
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.common.datastore.UserExpenseDataStore
import io.reactivex.Observable
import javax.inject.Inject

interface FetchUsersInteractor {
    fun fetchAll(): Observable<InteractorResult<MutableList<User>>>
}

class FetchUsersInteractorImpl @Inject constructor(private val userExpenseDataStore: UserExpenseDataStore)
    : FetchUsersInteractor {

    override fun fetchAll(): Observable<InteractorResult<MutableList<User>>> {
        return userExpenseDataStore
                .loadUsers()
                .map { InteractorResult(Success, it) }
                .startWith(InteractorResult(Loading))
    }
}