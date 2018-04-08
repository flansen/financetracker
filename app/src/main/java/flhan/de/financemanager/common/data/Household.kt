package flhan.de.financemanager.common.data

/**
 * Created by Florian on 09.09.2017.
 */
data class Household(
        var name: String = "",
        var id: String = "",
        var creator: String = "",
        var users: MutableMap<String, User> = mutableMapOf(),
        var secret: String = "")