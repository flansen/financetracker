package flhan.de.financemanager.expenseoverview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import flhan.de.financemanager.R
import flhan.de.financemanager.extensions.dpToPx

/**
 * Created by Florian on 06.10.2017.
 */
class ExpenseOverviewAdapter() : RecyclerView.Adapter<ExpenseOverviewViewHolder>() {
    override fun getItemCount(): Int {
        return 5
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