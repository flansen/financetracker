package flhan.de.financemanager.common.validators

import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by Florian on 29.09.2017.
 */
class EmailValidator @Inject constructor() : Validator<CharSequence> {

    override fun validate(toValidate: CharSequence): Boolean {
        val pattern = Pattern.compile(EMAIL_REGEX)
        return pattern.matcher(toValidate).matches()
    }

    companion object {
        private val EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
    }
}