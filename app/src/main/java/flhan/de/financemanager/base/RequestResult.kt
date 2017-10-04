package flhan.de.financemanager.base

/**
 * Created by fhansen on 04.10.17.
 */
data class RequestResult<T>(
        var result: T? = null,
        var exception: Throwable? = null
)