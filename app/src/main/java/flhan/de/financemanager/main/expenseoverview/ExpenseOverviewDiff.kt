package flhan.de.financemanager.main.expenseoverview

import android.os.Bundle
import android.support.v7.util.DiffUtil
import flhan.de.financemanager.common.data.Expense

class ExpenseOverviewDiff(private val oldExpenses: List<Expense>?,
                          private val newExpenses: List<Expense>?) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldExpenses?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newExpenses?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newExpenses?.get(newItemPosition)
        val oldItem = oldExpenses?.get(oldItemPosition)

        return oldItem?.id.equals(newItem?.id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newExpenses?.get(newItemPosition)
        val oldItem = oldExpenses?.get(oldItemPosition)
        return newItem?.equals(oldItem) ?: false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newItem = newExpenses?.get(newItemPosition)
        val oldItem = oldExpenses?.get(oldItemPosition)
        val bundle = Bundle()

        if (newItem?.amount != oldItem?.amount) {
            bundle.putDouble(Expense.AMOUNT_KEY, newItem!!.amount)
        }
        if (newItem?.cause != oldItem?.cause) {
            bundle.putString(Expense.CAUSE_KEY, newItem!!.cause)
        }

        return bundle
    }
}