package flhan.de.financemanager.main.expenseoverview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator.MATERIAL
import flhan.de.financemanager.R

/**
 * Created by Florian on 06.10.2017.
 */
class ExpenseOverviewAdapter : RecyclerView.Adapter<ExpenseOverviewViewHolder>() {

    var items: List<ExpenseOverviewItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExpenseOverviewViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.expense_overview_item, parent, false)
        return ExpenseOverviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseOverviewViewHolder?, position: Int) {
        val item = items[position]
        val bubbleSize = holder?.nameView?.context?.resources?.getDimension(R.dimen.bubble_size)?.toInt() ?: 0
        val generator = MATERIAL
        val drawable = TextDrawable.builder()
                .beginConfig()
                .width(bubbleSize)
                .height(bubbleSize)
                .endConfig()
                .buildRound(item.creator, generator.getColor(item.creator))
        holder?.nameView?.setImageDrawable(drawable)
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}

class ExpenseOverviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.expense_overview_item_name_view)
    lateinit var nameView: ImageView

    init {
        ButterKnife.bind(this, view)
    }
}