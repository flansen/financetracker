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
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import butterknife.ButterKnife
import butterknife.OnClick
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import flhan.de.financemanager.common.ui.LineListDivider
import flhan.de.financemanager.ui.main.ToolbarProvider
import flhan.de.financemanager.ui.main.expenses.createedit.CreateEditExpenseActivity
import kotlinx.android.synthetic.main.fragment_expense_overview.*
import javax.inject.Inject


class ExpenseOverviewFragment : Fragment(), ToolbarProvider {

    @Inject
    lateinit var factory: OverviewViewModelFactory

    private val screenWidth by lazy {
        val size = Point()
        activity?.windowManager?.defaultDisplay?.getSize(size)
        size.x
    }

    private lateinit var viewModel: ExpenseOverviewViewModel

    override val toolbar: Toolbar?
        get() = expensesToolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(ExpenseOverviewViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_expense_overview, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ExpenseOverviewAdapter { id -> presentCreateEdit(id) }

        expense_overview_recycler.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            addItemDecoration(LineListDivider(view.context))
            this.adapter = adapter
        }

        paymentItemView.minimumWidth = screenWidth
        viewModel.listItems.observe(this, Observer { listItems ->
            adapter.items = listItems ?: mutableListOf()
        })

        viewModel.paymentSums.observe(this, Observer { amounts ->
            if (amounts != null && !amounts.isEmpty()) {
                paymentItemView.visibility = VISIBLE
                paymentItemView.setItems(amounts)
            } else {
                paymentItemView.visibility = GONE
            }
        })
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.expense_overview_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.action_bill_all) {
            viewModel.billAll()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    @OnClick(R.id.expense_overview_fab)
    fun onCreateExpenseClicked() {
        presentCreateEdit()
    }

    private fun presentCreateEdit(id: String? = null) {
        val context = context ?: return
        val intent = CreateEditExpenseActivity.createIntent(context, id)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): ExpenseOverviewFragment = ExpenseOverviewFragment()
    }
}
