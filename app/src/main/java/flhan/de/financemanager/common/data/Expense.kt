package flhan.de.financemanager.common.data

import com.google.firebase.database.Exclude
import java.util.*


/**
 * Created by Florian on 07.10.2017.
 */
data class Expense(var cause: String = "",
                   var creator: String = "",
                   var createdAt: Date? = null,
                   var amount: Double? = null,
                   var id: String = "",
                   @get:Exclude var user: User? = null) {
    companion object {
        const val CAUSE = "cause"
        const val CREATOR = "creator"
        const val AMOUNT = "amount"
    }
}