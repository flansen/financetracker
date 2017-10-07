package flhan.de.financemanager.main.expenseoverview

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import flhan.de.financemanager.base.app
import flhan.de.financemanager.di.main.expenseoverview.ExpenseOverviewModule
import javax.inject.Inject


class ExpenseOverviewFragment : Fragment(), ExpenseOverviewContract.View {
    private val component by lazy { activity.app.appComponent.plus(ExpenseOverviewModule(this)) }

    @Inject
    lateinit var presenter: ExpenseOverviewContract.Presenter


    companion object {
        fun newInstance(): ExpenseOverviewFragment = ExpenseOverviewFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }
}
