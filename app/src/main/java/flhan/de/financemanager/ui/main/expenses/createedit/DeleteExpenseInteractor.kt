package flhan.de.financemanager.ui.main.expenses.createedit

import flhan.de.financemanager.common.datastore.UserExpenseDataStore
import io.reactivex.Single
import javax.inject.Inject

interface DeleteExpenseInteractor {
    fun delete(id: String): Single<Boolean>
}

class DeleteExpenseInteractorImpl @Inject constructor(
        private val userExpenseDataStore: UserExpenseDataStore
) : DeleteExpenseInteractor {

    override fun delete(id: String): Single<Boolean> {
        return userExpenseDataStore.deleteExpense(id)
    }
}