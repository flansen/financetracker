package flhan.de.financemanager.ui.main.expenses.createedit

import flhan.de.financemanager.common.datastore.ExpenseDataStore
import io.reactivex.Single
import javax.inject.Inject

interface DeleteExpenseInteractor {
    fun delete(id: String): Single<Boolean>
}

class DeleteExpenseInteractorImpl @Inject constructor(
        private val dataStore: ExpenseDataStore
) : DeleteExpenseInteractor {

    override fun delete(id: String): Single<Boolean> {
        return dataStore.deleteExpense(id)
    }
}