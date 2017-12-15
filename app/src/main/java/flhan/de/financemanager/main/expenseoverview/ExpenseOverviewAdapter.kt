package flhan.de.financemanager.main.expenseoverview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import flhan.de.financemanager.R
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.extensions.dpToPx

/**
 * Created by Florian on 06.10.2017.
 */
class ExpenseOverviewAdapter : RecyclerView.Adapter<ExpenseOverviewViewHolder>() {

    var items: List<Expense> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExpenseOverviewViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.expense_overview_item, parent, false)
        return ExpenseOverviewViewHolder(view)
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