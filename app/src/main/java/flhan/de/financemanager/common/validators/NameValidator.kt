package flhan.de.financemanager.common.validators

import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by Florian on 29.09.2017.
 */
class NameValidator @Inject constructor() : Validator<CharSequence> {
    private val nameRegex = "[a-zA-Z0-9]{3}.*"

    override fun validate(toValidate: CharSequence): Boolean {
        return Pattern.compile(nameRegex).matcher(toValidate).matches()
    }
}