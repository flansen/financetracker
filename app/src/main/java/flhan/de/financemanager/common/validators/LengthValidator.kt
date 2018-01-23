package flhan.de.financemanager.common.validators

import javax.inject.Inject

/**
 * Created by Florian on 29.09.2017.
 */
class LengthValidator @Inject constructor() : Validator<CharSequence?> {

    override fun validate(toValidate: CharSequence?): Boolean {
        return toValidate?.let { it.trim().length >= 3 } ?: false
    }
}