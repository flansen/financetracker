package flhan.de.financemanager.ui.main.expenses.createedit

import flhan.de.financemanager.common.datastore.RemoteDataStore
import io.reactivex.Single
import javax.inject.Inject

interface DeleteExpenseInteractor {
    fun delete(id: String): Single<Boolean>
}

class DeleteExpenseInteractorImpl @Inject constructor(
        private val remoteDataStore: RemoteDataStore
) : DeleteExpenseInteractor {

    override fun delete(id: String): Single<Boolean> {
        return remoteDataStore.deleteExpense(id)
    }
}