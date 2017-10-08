package flhan.de.financemanager.main.expenseoverview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import flhan.de.financemanager.R
import flhan.de.financemanager.common.Insert
import flhan.de.financemanager.common.ListEvent
import flhan.de.financemanager.common.Remove
import flhan.de.financemanager.common.Update
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.extensions.dpToPx
import io.reactivex.Observable

/**
 * Created by Florian on 06.10.2017.
 */
class ExpenseOverviewAdapter(expenses: Observable<ListEvent<Expense>>) : RecyclerView.Adapter<ExpenseOverviewViewHolder>() {
    val items: MutableList<Expense>

    init {
        items = mutableListOf()
        expenses.subscribe { listEvent ->
            when (listEvent) {
                is Insert -> {
                    items.add(0, listEvent.obj)
                    notifyItemInserted(0)
                }
                is Update -> {
                    val itemIndex = items.indexOfFirst { item -> item.id == listEvent.obj.id }
                    items[itemIndex] = listEvent.obj
                    notifyItemChanged(itemIndex)
                }
                is Remove -> {
                    val itemIndex = items.indexOfFirst { item -> item.id == listEvent.id }
                    items.removeAt(itemIndex)
                    notifyItemRemoved(itemIndex)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ExpenseOverviewViewHolder?, position: Int) {
        val generator = ColorGenerator.MATERIAL // or use DEFAULT
        val drawable = TextDrawable.builder()
                .beginConfig()
                .width(48.dpToPx())
                .height(48.dpToPx())
                .endConfig()
                .buildRound("a", generator.getColor("a"))
        holder?.nameView?.setImageDrawable(drawable)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExpenseOverviewViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.expense_overview_item, parent, false)
        return ExpenseOverviewViewHolder(view)
    }
}

class ExpenseOverviewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val nameView: ImageView by lazy { view.findViewById<ImageView>(R.id.expense_overview_item_name_view) }
}