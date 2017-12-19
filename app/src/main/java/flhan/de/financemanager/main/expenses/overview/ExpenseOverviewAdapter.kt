package flhan.de.financemanager.main.expenses.overview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator.MATERIAL
import flhan.de.financemanager.R

/**
 * Created by Florian on 06.10.2017.
 */
class ExpenseOverviewAdapter(private val clickListener: (String) -> Unit) : RecyclerView.Adapter<ExpenseOverviewAdapter.ExpenseOverviewViewHolder>() {

    var items: List<ExpenseOverviewItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ExpenseOverviewViewHolder(parent!!)

    override fun onBindViewHolder(holder: ExpenseOverviewViewHolder?, position: Int) {
        holder?.let {
            val item = items[position]
            val bubbleSize = holder.name.context.resources.getDimension(R.dimen.bubble_size).toInt()
            val generator = MATERIAL
            val drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(bubbleSize)
                    .height(bubbleSize)
                    .endConfig()
                    .buildRound(item.creator, generator.getColor(item.creator))
            holder.apply {
                name.setImageDrawable(drawable)
                amount.text = item.amount
                cause.text = item.cause
                date.text = item.date
                root.setOnClickListener { clickListener(item.id) }
            }
        }
    }

    override fun getItemCount() = items.count()

    inner class ExpenseOverviewViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.expense_overview_item, parent, false)) {

        @BindView(R.id.overview_item_name)
        lateinit var name: ImageView

        @BindView(R.id.overview_item_amount)
        lateinit var amount: TextView

        @BindView(R.id.overview_item_cause)
        lateinit var cause: TextView

        @BindView(R.id.overview_item_date)
        lateinit var date: TextView

        @BindView(R.id.overview_item_root)
        lateinit var root: ViewGroup

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}