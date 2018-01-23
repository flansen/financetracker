package flhan.de.financemanager.ui.main.expenses.overview

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator.MATERIAL
import flhan.de.financemanager.R
import flhan.de.financemanager.common.extensions.inflate
import kotlinx.android.synthetic.main.expense_overview_item.view.*

/**
 * Created by Florian on 06.10.2017.
 */
class ExpenseOverviewAdapter(private val clickListener: (String) -> Unit) : RecyclerView.Adapter<ExpenseOverviewAdapter.ExpenseOverviewViewHolder>() {

    var items: List<ExpenseOverviewItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ExpenseOverviewViewHolder(parent)

    override fun onBindViewHolder(holder: ExpenseOverviewViewHolder?, position: Int) {
        holder?.apply {
            val item = items[position]
            val bubbleSize = itemView.context.resources.getDimension(R.dimen.bubble_size).toInt()
            val generator = MATERIAL
            val drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(bubbleSize)
                    .height(bubbleSize)
                    .textColor(ContextCompat.getColor(itemView.context, android.R.color.black))
                    .endConfig()
                    .buildRound(item.creator, generator.getColor(item.creatorId))
            itemView.apply {
                overview_item_name.setImageDrawable(drawable)
                overview_item_amount.text = item.amount.displayString
                overview_item_cause.text = item.cause
                overview_item_date.text = item.date
                setOnClickListener { clickListener(item.id) }
            }
        }
    }

    override fun getItemCount() = items.count()

    inner class ExpenseOverviewViewHolder(parent: ViewGroup?) : RecyclerView.ViewHolder(parent inflate LAYOUT_RES_ID)

    companion object {
        const val LAYOUT_RES_ID = R.layout.expense_overview_item
    }
}