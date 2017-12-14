package flhan.de.financemanager.common.validators

import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by Florian on 29.09.2017.
 */
class NameValidator @Inject constructor() : Validator<CharSequence> {

    override fun validate(toValidate: CharSequence): Boolean {
        return Pattern.compile(NAME_REGEX).matcher(toValidate).matches()
    }

    companion object {
        private const val NAME_REGEX = "[a-zA-Z0-9]{3}.*"
    }
}