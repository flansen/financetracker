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
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import flhan.de.financemanager.common.extensions.applyWhiteStyle
import flhan.de.financemanager.common.ui.LineListDivider
import flhan.de.financemanager.ui.main.expenses.createedit.CreateEditExpenseActivity
import kotlinx.android.synthetic.main.fragment_expense_overview.*
import javax.inject.Inject


class ExpenseOverviewFragment : Fragment() {

    @Inject
    lateinit var factory: OverviewViewModelFactory

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private val screenWidth by lazy {
        val size = Point()
        activity?.windowManager?.defaultDisplay?.getSize(size)
        size.x
    }

    private lateinit var viewModel: ExpenseOverviewViewModel

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
        (activity as AppCompatActivity).supportActionBar?.applyWhiteStyle()
        paymentItemView.minimumWidth = screenWidth
        val adapter = ExpenseOverviewAdapter({ id -> presentCreateEdit(id) })
        expense_overview_recycler.adapter = adapter
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
        presentCreateEdit(null)
    }

    private fun presentCreateEdit(id: String?) {
        val intent = CreateEditExpenseActivity.createIntent(context!!, id)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): ExpenseOverviewFragment = ExpenseOverviewFragment()
    }
}
