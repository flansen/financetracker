package flhan.de.financemanager.base


/**
 * Created by fhansen on 04.10.17.
 */
data class InteractorResult<T>(
        var status: InteractorStatus,
        var result: T? = null,
        var exception: Throwable? = null
)