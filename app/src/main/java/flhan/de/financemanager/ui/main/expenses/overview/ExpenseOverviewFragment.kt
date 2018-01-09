package flhan.de.financemanager.ui.main.expenses.overview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import flhan.de.financemanager.common.ui.LineListDivider
import flhan.de.financemanager.ui.main.expenses.createedit.CreateEditExpenseActivity
import kotlinx.android.synthetic.main.fragment_expense_overview.*
import javax.inject.Inject


class ExpenseOverviewFragment : Fragment() {

    @Inject
    lateinit var factory: OverviewViewModelFactory

    private val screenWidth by lazy {
        val size = Point()
        activity?.windowManager?.defaultDisplay?.getSize(size)
        size.x
    }

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
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expense_overview_recycler.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        expense_overview_recycler.addItemDecoration(LineListDivider(context!!))
    }

    override fun onStart() {
        super.onStart()
        paymentItemView.minimumWidth = screenWidth
        val adapter = ExpenseOverviewAdapter({ id -> presentCreateEdit(id) })
        expense_overview_recycler.adapter = adapter
        viewModel.listItems.observe(this, Observer { listItems ->
            adapter.items = listItems ?: mutableListOf()
        })

        viewModel.paymentSums.observe(this, Observer { amounts ->
            if (amounts != null && !amounts.isEmpty()) {
                paymentItemView.visibility = VISIBLE
            } else {
                paymentItemView.visibility = GONE
            }
            paymentItemView.addOrUpdateItems(amounts)
        })
    }

    @OnClick(R.id.expense_overview_fab)
    fun onCreateExpenseClicked() {
        presentCreateEdit(null)
    }

    private fun presentCreateEdit(id: String?) {
        val intent = CreateEditExpenseActivity.createIntent(context!!, id)
        startActivity(intent)
    }
}
