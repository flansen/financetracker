package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import flhan.de.financemanager.R

class ShoppingItemOverviewAdapter(private val clickListener: (String) -> Unit,
                                  private val checkedListener: (ShoppingOverviewItem) -> Unit) : RecyclerView.Adapter<ShoppingItemOverviewAdapter.ShoppingItemHolder>() {

    var items: List<ShoppingOverviewItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShoppingItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.shopping_item_overview_item, parent, false))

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ShoppingItemHolder, position: Int) = holder.bind(items[position], checkedListener)

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }

    inner class ShoppingItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.shopping_item_name)
        lateinit var name: TextView

        @BindView(R.id.shopping_item_checked)
        lateinit var checked: CheckBox

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(shoppingOverviewItem: ShoppingOverviewItem, checkedCallback: (ShoppingOverviewItem) -> Unit) {
            name.text = shoppingOverviewItem.name
            checked.isChecked = shoppingOverviewItem.done
            itemView.setOnClickListener { clickListener(shoppingOverviewItem.id) }
            checked.setOnClickListener { _ -> checkedCallback(shoppingOverviewItem) }
        }
    }
}

