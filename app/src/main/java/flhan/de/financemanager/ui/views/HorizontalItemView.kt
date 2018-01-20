package flhan.de.financemanager.ui.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.amulyakhare.textdrawable.TextDrawable
import flhan.de.financemanager.R
import flhan.de.financemanager.ui.main.expenses.overview.ExpensePaymentItem
import kotlinx.android.synthetic.main.payment_item.view.*

class HorizontalItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val views: MutableMap<String, Int> = mutableMapOf()

    fun setItems(items: List<ExpensePaymentItem>) {
        val userIds = items.map { it.userId }
        val keysToRemove = views.keys.filterNot { userIds.contains(it) }
        keysToRemove.forEach { removeViewByKey(it) }
        items.forEach { addOrUpdateItem(it) }
    }

    private fun addOrUpdateItem(item: ExpensePaymentItem) {
        lateinit var view: View
        if (views.containsKey(item.userId)) {
            view = getChildAt(views[item.userId]!!)
        } else {
            view = createView()
            val index = childCount
            addView(view, index)
            views[item.userId] = index
        }
        bindView(view, item)
    }

    private fun bindView(view: View, item: ExpensePaymentItem) {
        val bubbleSize = context.resources.getDimension(R.dimen.bubble_size).toInt()
        val drawable = TextDrawable.builder()
                .beginConfig()
                .width(bubbleSize)
                .height(bubbleSize)
                .textColor(ContextCompat.getColor(context, android.R.color.black))
                .endConfig()
                .buildRound(item.name, ContextCompat.getColor(context, android.R.color.white))
        view.paymentItemImage.setImageDrawable(drawable)
        view.paymentItemAmount.text = item.amount
    }

    private fun createView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.payment_item, this, false)
        val layoutParams = (view.layoutParams as LinearLayout.LayoutParams)
        layoutParams.weight = 1f
        view.layoutParams = layoutParams
        return view
    }

    private fun removeViewByKey(key: String) {
        removeViewAt(views[key]!!)
        views.remove(key)
    }
}