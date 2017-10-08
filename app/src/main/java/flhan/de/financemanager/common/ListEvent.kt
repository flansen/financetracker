package flhan.de.financemanager.common

/**
 * Created by Florian on 08.10.2017.
 */
sealed class ListEvent<T> {}

data class Insert<T>(val obj: T) : ListEvent<T>()
data class Update<T>(val obj: T) : ListEvent<T>()
data class Remove<T>(val id: String) : ListEvent<T>()