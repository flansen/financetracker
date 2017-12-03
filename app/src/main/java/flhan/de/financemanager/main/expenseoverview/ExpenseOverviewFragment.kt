package flhan.de.financemanager.main.expenseoverview

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_overview.*
import javax.inject.Inject

//TODO: Implement Base-Class and add CompositeDisposable to it
class ExpenseOverviewFragment : Fragment(), ExpenseOverviewContract.View {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var adapter: ExpenseOverviewAdapter? = null

    @Inject
    lateinit var presenter: ExpenseOverviewContract.Presenter


    companion object {
        fun newInstance(): ExpenseOverviewFragment = ExpenseOverviewFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.activity_overview, container, false)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        expense_overview_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ExpenseOverviewAdapter(presenter.expenses)
        expense_overview_recycler.adapter = adapter
    }

    override fun onDestroy() {
        disposable.dispose()
        adapter?.dispose()
        super.onDestroy()
    }
}
