package flhan.de.financemanager.ui.main.expenses.overview

import com.nhaarman.mockito_kotlin.whenever
import flhan.de.financemanager.common.data.Expense
import flhan.de.financemanager.common.data.User
import flhan.de.financemanager.common.datastore.RemoteDataStore
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.text.DecimalFormat
import java.util.*

class ExpenseOverviewInteractorTest {

    @Mock
    private lateinit var remoteDatastore: RemoteDataStore

    private lateinit var sut: ExpenseOverviewInteractor

    private val decimalSeparator by lazy {
        val format = DecimalFormat.getInstance() as DecimalFormat
        format.decimalFormatSymbols.decimalSeparator
    }

    private val currencySymbol by lazy {
        (DecimalFormat.getInstance() as DecimalFormat).currency.symbol
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sut = ExpenseOverviewInteractorImpl(remoteDatastore)
    }

    @Test
    fun testPaymentItems() {
        val testObserver = TestObserver<List<ExpensePaymentItem>>()
        val user1 = User("Name1", "email1", "uid1")
        val user2 = User("Name2", "email2", "uid2")
        val expected1 = "3" + decimalSeparator + "30 " + currencySymbol
        val expected2 = "19" + decimalSeparator + "20 " + currencySymbol
        val expenses = mutableListOf(
                Expense("Cause1", "email1", Date(), 2.2, "id1", user1),
                Expense("Cause2", "email2", Date(), 6.0, "id2", user2),
                Expense("Cause3", "email2", Date(), 13.2, "id3", user2),
                Expense("Cause4", "email1", Date(), 1.1, "id4", user1)
        )
        whenever(remoteDatastore.loadExpenses()).thenReturn(Observable.just(expenses))

        sut.getPaymentItems().subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val result = testObserver.values()[0]
        assertEquals(2, result.count())

        val resultValue1 = result[0]
        assertEquals("Name1", resultValue1.name)
        assertEquals("uid1", resultValue1.userId)
        assertEquals(expected1, resultValue1.amount)

        val resultValue2 = result[1]
        assertEquals("Name2", resultValue2.name)
        assertEquals("uid2", resultValue2.userId)
        assertEquals(expected2, resultValue2.amount)
    }
}