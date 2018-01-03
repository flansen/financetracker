package flhan.de.financemanager.ui.main.expenses.createedit

import flhan.de.financemanager.base.InteractorResult
import flhan.de.financemanager.base.InteractorStatus.Loading
import flhan.de.financemanager.base.InteractorStatus.Success
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.common.datastore.RemoteDataStore
import io.reactivex.Observable
import javax.inject.Inject

interface FetchUsersInteractor {
    fun fetchAll(): Observable<InteractorResult<MutableList<User>>>
}

class FetchUsersInteractorImpl @Inject constructor(private val remoteDataStore: RemoteDataStore)
    : FetchUsersInteractor {

    override fun fetchAll(): Observable<InteractorResult<MutableList<User>>> {
        return remoteDataStore
                .loadUsers()
                .map { InteractorResult(Success, it) }
                .startWith(InteractorResult(Loading))
    }
}