package flhan.de.financemanager.common.validators

import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Florian on 30.09.2017.
 */
class NameValidatorTest {
    lateinit var sut: NameValidator

    @Before
    fun setUp() {
        sut = NameValidator()
    }

    @Test
    fun `invalid name too short`() {
        val name = "ab"
        Assert.assertFalse(sut.validate(name))
    }

    @Test
    fun `invalid name whitespaces`() {
        val name = "ab  "
        Assert.assertFalse(sut.validate(name))
    }


    @Test
    fun `valid name`() {
        val name = "tes"
        Assert.assertTrue(sut.validate(name))
    }
}