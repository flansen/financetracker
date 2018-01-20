package flhan.de.financemanager.common.data

import java.util.*

data class Billing(
        val items: List<BillingItem>,
        val date: Date,
        val creatorId: String,
        var id: String? = null
)