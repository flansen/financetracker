package flhan.de.financemanager.common.data

import com.google.firebase.database.Exclude


/**
 * Created by Florian on 07.10.2017.
 */
data class Expense(var cause: String = "",
                   var creator: String = "",
        //var createdAt: Date,
                   var amount: Double = 0.0,
                   var id: String = "",
                   @get:Exclude var user: User? = null)