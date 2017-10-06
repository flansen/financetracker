package flhan.de.financemanager.expenseoverview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import flhan.de.financemanager.R
import flhan.de.financemanager.base.app
import flhan.de.financemanager.di.expenseoverview.ExpenseOverviewModule
import javax.inject.Inject


class ExpenseOverviewActivity : AppCompatActivity(), ExpenseOverviewContract.View {
    private val component by lazy { app.appComponent.plus(ExpenseOverviewModule(this)) }

    @Inject
    lateinit var presenter: ExpenseOverviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        component.inject(this)
    }
}
