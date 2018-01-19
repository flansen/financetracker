package flhan.de.financemanager

import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.data.User
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
    }

    @Test
    fun testList() {
        val user1 = User("name", "email", "id")
        val user2 = User("name", "email", "id")
        val user3 = User("name1", "email1", "id1")

        val expense1 = Expense("cause", "creator", null, 1.0, "id", user1)
        val expense2 = Expense("cause", "creator", null, 1.0, "id", user2)
        val expense3 = Expense("cause", "creator", null, 1.0, "id", user3)
        val expense4 = Expense("cause", "creator", null, 1.0, "id", user1)

        val list = mutableListOf(expense1, expense2, expense3, expense4)

        val map = list.groupBy { it.user }
    }
}
