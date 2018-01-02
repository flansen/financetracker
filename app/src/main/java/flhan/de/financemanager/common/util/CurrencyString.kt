package flhan.de.financemanager.common.util

import java.text.DecimalFormat
import java.util.*

class CurrencyString(
        initialValue: String = "",
        private val locale: Locale = Locale.getDefault()) {

    constructor(initialValue: Double?) : this(initialValue?.toString() ?: "")

    var displayString: String
        get() {
            val amountString = if (baseString.isEmpty()) {
                "0"
            } else {
                baseString
            }
            val amountValue = amountString.toDouble() / 100
            val decimalFormat = DecimalFormat.getInstance(locale) as DecimalFormat
            decimalFormat.applyPattern(CURRENCY_NUMBER_PATTERN)
            val currencyAmount = decimalFormat.format(amountValue)
            return String.format(CURRENCY_PATTERN, currencyAmount, decimalFormat.currency.symbol)
        }
        set(value) {
            baseString = value
        }

    var amount: Double? = null
        get() {
            return if (baseString.isEmpty()) {
                0.0
            } else {
                baseString.toDouble() / 100
            }
        }
        private set

    var baseString: String
        private set

    init {
        var valueString = initialValue.replace(',', '.')
        if (valueString.contains('.')) {
            val decimalsString = valueString.split('.')[1]
            val numberOfDecimals = decimalsString.length
            val index = valueString.indexOfFirst { it == '.' }
            valueString = valueString.removeRange(index, index + 1)
            for (i in 1..(2 - numberOfDecimals)) {
                valueString += "0"
            }
        }
        baseString = valueString
    }

    companion object {
        const val CURRENCY_NUMBER_PATTERN = "###,##0.00"
        const val CURRENCY_PATTERN = "%s %s"
    }
}