package flhan.de.financemanager.common.validators

import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by Florian on 29.09.2017.
 */
class LengthValidator @Inject constructor() : Validator<CharSequence?> {

    override fun validate(toValidate: CharSequence?): Boolean {
        val validationTarget = toValidate?.let { it } ?: return false
        return Pattern.compile(NAME_REGEX).matcher(validationTarget).matches()
    }

    companion object {
        private const val NAME_REGEX = "[a-zA-Z0-9]{3}.*"
    }
}