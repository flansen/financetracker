package flhan.de.financemanager.createjoinhousehold

/**
 * Created by fhansen on 05.10.17.
 */

enum class ErrorType {
    NoSuchHousehold,
    Unknown,
    None
}

data class CreateJoinErrorState(
        val type: ErrorType,
        val message: String? = ""
)