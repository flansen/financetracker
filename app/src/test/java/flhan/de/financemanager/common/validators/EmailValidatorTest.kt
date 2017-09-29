package flhan.de.financemanager.common.validators

import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Florian on 29.09.2017.
 */
class EmailValidatorTest {
    lateinit var sut: EmailValidator

    @Before
    fun setUp() {
        sut = EmailValidator()
    }

    @Test
    fun `invalid no @ sign`() {
        var emailWithoutAt = "testtest.de"
        Assert.assertFalse(sut.validate(emailWithoutAt))
    }

    @Test
    fun `invalid too short`() {
        var shortMail = "a@b.e"
        Assert.assertFalse(sut.validate(shortMail))
    }

    @Test
    fun `invalid short domain`() {
        var invalidDomainMail = "abc@bef.e"
        Assert.assertFalse(sut.validate(invalidDomainMail))
    }

    @Test
    fun `valid`() {
        var validMail = "test@user.de"
        Assert.assertFalse(sut.validate(validMail))
    }
}