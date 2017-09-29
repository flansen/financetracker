package flhan.de.financemanager.common.validators

/**
 * Created by Florian on 29.09.2017.
 */
interface Validator<T> {
    fun validate(toValidate: T): Boolean
}