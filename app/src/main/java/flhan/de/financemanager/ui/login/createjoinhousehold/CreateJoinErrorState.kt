package flhan.de.financemanager.ui.login.createjoinhousehold

/**
 * Created by fhansen on 05.10.17.
 */

enum class ErrorType {
    NoSuchHousehold,
    Unknown,
    InvalidSecret,
    None
}

data class CreateJoinErrorState(
        val type: ErrorType,
        val message: String? = ""
)