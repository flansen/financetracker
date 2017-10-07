package flhan.de.financemanager.common.data

import java.util.*

/**
 * Created by Florian on 07.10.2017.
 */
data class Expense(var cause: String,
                   var creator: String,
                   var createdAt: Date,
                   var amount: Double,
                   var id: String = "")