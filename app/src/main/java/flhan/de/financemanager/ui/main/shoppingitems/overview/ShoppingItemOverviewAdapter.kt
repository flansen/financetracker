package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import flhan.de.financemanager.R

class ShoppingItemOverviewAdapter(
        private val clickListener: (String) -> Unit,
        private val checkedListener: (ShoppingOverviewItem.Data) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<ShoppingOverviewItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.shopping_item_overview_item -> ShoppingItemHolder(view)
            R.layout.shopping_item_overview_group -> ShoppingItemGroupHolder(view)
            else -> {
                throw IllegalArgumentException("No VeiwType for $viewType")
            }
        }
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShoppingItemHolder -> holder.bind(items[position] as ShoppingOverviewItem.Data, checkedListener)
            is ShoppingItemGroupHolder -> holder.bind(items[position] as ShoppingOverviewItem.Group)
        }
    }

    override fun getItemId(position: Int): Long {
        val item = items[position]
        return when (item) {
            is ShoppingOverviewItem.Data -> item.item.id.hashCode().toLong()
            is ShoppingOverviewItem.Group -> item.name.hashCode().toLong()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ShoppingOverviewItem.Data -> R.layout.shopping_item_overview_item
            is ShoppingOverviewItem.Group -> R.layout.shopping_item_overview_group
        }
    }

    inner class ShoppingItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.shopping_item_name)
        lateinit var nameTextView: TextView

        @BindView(R.id.shopping_item_checked)
        lateinit var checked: CheckBox

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(item: ShoppingOverviewItem.Data, checkedCallback: (ShoppingOverviewItem.Data) -> Unit) {
            with(item.item) {
                nameTextView.text = name
                checked.isChecked = done
                itemView.setOnClickListener { clickListener(id) }
                if (done) {
                    nameTextView.paintFlags = nameTextView.paintFlags or STRIKE_THRU_TEXT_FLAG
                } else {
                    nameTextView.paintFlags = nameTextView.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
            checked.setOnClickListener { _ -> checkedCallback(item) }
        }
    }

    inner class ShoppingItemGroupHolder(val view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.shopping_item_group_name)
        lateinit var groupName: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(item: ShoppingOverviewItem.Group) {
            groupName.setText(item.name)
        }
    }
}

