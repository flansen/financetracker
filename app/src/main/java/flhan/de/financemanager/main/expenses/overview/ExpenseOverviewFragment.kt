package flhan.de.financemanager.main.expenses.overview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindString
import butterknife.ButterKnife
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import flhan.de.financemanager.common.LineListDivider
import flhan.de.financemanager.main.expenses.createedit.CreateEditExpenseActivity
import kotlinx.android.synthetic.main.fragment_expense_overview.*
import javax.inject.Inject

class ExpenseOverviewFragment : Fragment() {

    @Inject
    lateinit var factory: OverviewViewModelFactory

    @BindString(R.string.expense_overview_title)
    lateinit var title: String

    private lateinit var viewModel: ExpenseOverviewViewModel

    companion object {
        fun newInstance(): ExpenseOverviewFragment = ExpenseOverviewFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(ExpenseOverviewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_expense_overview, container, false)
        ButterKnife.bind(this, view)
        (activity as AppCompatActivity).supportActionBar?.title = title
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expense_overview_recycler.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        expense_overview_recycler.addItemDecoration(LineListDivider(context!!))
    }

    override fun onStart() {
        super.onStart()
        val adapter = ExpenseOverviewAdapter({ id -> presentCreateEdit(id) })
        expense_overview_recycler.adapter = adapter
        viewModel.listItems.observe(this, Observer { listItems ->
            adapter.items = listItems ?: mutableListOf()
        })
    }

    private fun presentCreateEdit(id: String) {
        val intent = CreateEditExpenseActivity.createIntent(context!!, id)
        startActivity(intent)
    }
}
