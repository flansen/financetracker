package flhan.de.financemanager.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import flhan.de.financemanager.R
import flhan.de.financemanager.ui.main.expenses.overview.ExpensePaymentItem
import kotlinx.android.synthetic.main.payment_item.view.*

class HorizontalItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val views: MutableMap<String, Int> = mutableMapOf()

    fun addOrUpdateItem(item: ExpensePaymentItem) {
        lateinit var view: View
        if (views.containsKey(item.userId)) {
            view = getChildAt(views[item.userId]!!)
        } else {
            view = createView()
            val index = childCount
            addView(view, index)
            views.put(item.userId, index)
        }
        bindView(view, item)
    }

    private fun bindView(view: View, item: ExpensePaymentItem) {
        val bubbleSize = context.resources.getDimension(R.dimen.bubble_size).toInt()
        val generator = ColorGenerator.MATERIAL
        val drawable = TextDrawable.builder()
                .beginConfig()
                .width(bubbleSize)
                .height(bubbleSize)
                .endConfig()
                .buildRound(item.name, generator.getColor(item.userId))
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

    fun addOrUpdateItems(items: List<ExpensePaymentItem>?) {
        items?.forEach { addOrUpdateItem(it) }
    }
}