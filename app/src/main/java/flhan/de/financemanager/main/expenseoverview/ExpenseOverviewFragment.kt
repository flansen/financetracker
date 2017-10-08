package flhan.de.financemanager.main.expenseoverview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import flhan.de.financemanager.R
import flhan.de.financemanager.base.app
import flhan.de.financemanager.di.main.expenseoverview.ExpenseOverviewModule
import kotlinx.android.synthetic.main.activity_overview.*
import javax.inject.Inject


class ExpenseOverviewFragment : Fragment(), ExpenseOverviewContract.View {
    private val component by lazy { activity.app.appComponent.plus(ExpenseOverviewModule(this)) }

    @Inject
    lateinit var presenter: ExpenseOverviewContract.Presenter


    companion object {
        fun newInstance(): ExpenseOverviewFragment = ExpenseOverviewFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.activity_overview, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        expense_overview_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = ExpenseOverviewAdapter(presenter.expenses)
        expense_overview_recycler.adapter = adapter

    }
}
