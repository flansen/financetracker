package flhan.de.financemanager

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val s = myHigherOrderFun { "the string is $it" }
        println(s)
    }

    fun myHigherOrderFun(functionArg: (Int) -> String) = functionArg(5)
}
