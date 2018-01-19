package flhan.de.financemanager.common.data

/**
 * Created by Florian on 09.09.2017.
 */
data class User(
        var name: String = "",
        var email: String = "",
        var id: String = "") {

    val displayName: String
        get() {
            var nameString = ""
            name.split(' ').forEach { nameString += it[0] }
            return nameString
        }
}