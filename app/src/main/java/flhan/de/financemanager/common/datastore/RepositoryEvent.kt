package flhan.de.financemanager.common.datastore

/**
 * Created by Florian on 07.10.2017.
 */
open class RepositoryEvent<T>
data class Delete<T>(val id: String): RepositoryEvent<T>()
data class Update<T>(val obj: T): RepositoryEvent<T>()
data class Create<T>(val obj: T): RepositoryEvent<T>()

