package flhan.de.financemanager.common.util

import java.text.DecimalFormat
import java.util.*

class CurrencyString(
        initialString: String = "",
        private val locale: Locale = Locale.getDefault()) {

    constructor(initialDouble: Double?) : this(initialDouble?.toString() ?: "")

    var displayString: String
        get() {
            val amountString = if (baseString.isEmpty()) {
                "0"
            } else {
                baseString
            }
            val amountDouble = amountString.toDouble() / 100
            val decimalFormat = DecimalFormat.getInstance(locale) as DecimalFormat
            decimalFormat.applyPattern(CURRENCY_NUMBER_PATTERN)
            val currencyAmount = decimalFormat.format(amountDouble)
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
        if (initialString.contains('.')) {
            var valueString = initialString.replace(',', '.')
            valueString = fillUpTrailingZeros(valueString)
            baseString = valueString
        } else {
            baseString = initialString
        }
    }

    private fun fillUpTrailingZeros(amountString: String): String {
        var valueString = amountString
        val decimalsString = valueString.split('.')[1]
        val numberOfDecimals = decimalsString.length
        val index = valueString.indexOfFirst { it == '.' }
        valueString = valueString.removeRange(index, index + 1)
        for (i in 1..(2 - numberOfDecimals)) {
            valueString += "0"
        }
        return valueString
    }

    companion object {
        const val CURRENCY_NUMBER_PATTERN = "###,##0.00"
        const val CURRENCY_PATTERN = "%s %s"
    }
}