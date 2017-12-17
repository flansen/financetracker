package flhan.de.financemanager.main.expenseoverview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import kotlinx.android.synthetic.main.activity_overview.*
import javax.inject.Inject

class ExpenseOverviewFragment : Fragment() {

    @Inject
    lateinit var factory: OverviewViewModelFactory
    lateinit var viewModel: ExpenseOverviewViewModel

    companion object {
        fun newInstance(): ExpenseOverviewFragment = ExpenseOverviewFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(ExpenseOverviewViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_overview, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expense_overview_recycler.layoutManager = LinearLayoutManager(context, VERTICAL, false)
    }

    override fun onStart() {
        super.onStart()
        val adapter = ExpenseOverviewAdapter()
        expense_overview_recycler.adapter = adapter
        viewModel.listItems.observe(this, Observer { listItems ->
            adapter.items = listItems ?: mutableListOf()
        })
    }
}
