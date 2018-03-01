package flhan.de.financemanager.ui.main.expenses.createedit

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Loading
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.common.datastore.UserDataStore
import io.reactivex.Observable
import javax.inject.Inject

interface FetchUsersInteractor {
    fun fetchAll(): Observable<InteractorResult<MutableList<User>>>
}

class FetchUsersInteractorImpl @Inject constructor(private val dataStore: UserDataStore)
    : FetchUsersInteractor {

    override fun fetchAll(): Observable<InteractorResult<MutableList<User>>> {
        return dataStore
                .loadUsers()
                .map { InteractorResult(Success, it) }
                .startWith(InteractorResult(Loading))
    }
}