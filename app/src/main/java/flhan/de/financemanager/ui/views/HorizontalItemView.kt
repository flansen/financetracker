package flhan.de.financemanager.ui.views

import android.content.Context
import android.graphics.Typeface
import android.graphics.Typeface.NORMAL
import android.graphics.Typeface.create
import android.support.v4.content.ContextCompat.getColor
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
    private val bubbleTypeface: Typeface by lazy { create(TYPEFACE_NAME, NORMAL) }

    fun setItems(items: List<ExpensePaymentItem>) {
        if (items.isEmpty()) {
            removeAllViews()
            views.clear()
            return
        }
        val userIds = items.map { it.userId }
        val keysToRemove = views.keys.filterNot { userIds.contains(it) }
        keysToRemove.forEach { removeViewByKey(it) }
        items.forEach { addOrUpdateItem(it) }
    }

    private fun addOrUpdateItem(item: ExpensePaymentItem) {
        lateinit var view: View
        if (views.containsKey(item.userId)) {
            val v = getChildAt(views[item.userId]!!)
            if (v == null) {
                views.remove(item.userId)
                return
            } else {
                view = v
            }
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
        val generator = ColorGenerator.MATERIAL
        val drawable = TextDrawable.builder()
                .beginConfig()
                .width(bubbleSize)
                .height(bubbleSize)
                .textColor(generator.getColor(item.userId))
                .useFont(bubbleTypeface)
                .endConfig()
                .buildRound(item.name, getColor(context, android.R.color.white))
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

    companion object {
        const val TYPEFACE_NAME = "sans-serif-medium"
    }
}