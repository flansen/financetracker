package flhan.de.financemanager.common.validators

import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by Florian on 29.09.2017.
 */
class EmailValidator @Inject constructor() : Validator<CharSequence> {
    val emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"

    override fun validate(toValidate: CharSequence): Boolean {
        val pattern = Pattern.compile(emailRegex)
        return pattern.matcher(toValidate).matches()
    }
}