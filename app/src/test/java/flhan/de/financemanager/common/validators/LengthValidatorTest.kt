package flhan.de.financemanager.common.validators

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by Florian on 30.09.2017.
 */
class LengthValidatorTest {
    lateinit var sut: LengthValidator

    @Before
    fun setUp() {
        sut = LengthValidator()
    }

    @Test
    fun `invalid length too short`() {
        val name = "ab"
        assertFalse(sut.validate(name))
    }

    @Test
    fun `invalid null`() {
        assertFalse(sut.validate(null))
    }

    @Test
    fun `invalid length whitespaces`() {
        val name = "ab  "
        assertFalse(sut.validate(name))
    }


    @Test
    fun `valid length`() {
        val name = "tes"
        assertTrue(sut.validate(name))
    }
}