package flhan.de.financemanager.common.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class CurrencyString(
        initialString: String = "",
        private val locale: Locale = Locale.getDefault()) {

    constructor(initialDouble: Double?) : this(initialDouble?.let { NUMBER_FORMAT.format(it).toString() } ?: "")

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

    val amount: Double?
        get() {
            return if (baseString.isEmpty()) {
                0.0
            } else {
                baseString.toDouble() / 100
            }
        }

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
        val numberOfDecimals = amountString.numberOfDecimalPlaces()
        val index = amountString.indexOfFirst { it == '.' }
        var valueString = amountString.removeRange(index, index + 1)
        for (i in 1..(2 - numberOfDecimals)) {
            valueString += "0"
        }
        return valueString
    }

    private fun String.numberOfDecimalPlaces(): Int {
        val decimalsString = this.split('.')[1]
        return decimalsString.length
    }

    companion object {
        const val CURRENCY_NUMBER_PATTERN = "###,##0.00"
        const val CURRENCY_PATTERN = "%s %s"
        private val NUMBER_FORMAT = DecimalFormat("0.00", DecimalFormatSymbols(Locale.US))
    }
}