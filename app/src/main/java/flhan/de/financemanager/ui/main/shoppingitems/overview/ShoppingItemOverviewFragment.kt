package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import flhan.de.financemanager.ui.main.ToolbarProvider
import flhan.de.financemanager.ui.main.shoppingitems.createedit.CreateEditShoppingItemActivity
import kotlinx.android.synthetic.main.fragment_shopping_item_overview.*
import javax.inject.Inject

class ShoppingItemOverviewFragment : Fragment(), ToolbarProvider {

    @Inject
    lateinit var factory: ShoppingItemOverviewViewModelFactory

    private lateinit var viewModel: ShoppingItemOverviewViewModel

    override val toolbar: Toolbar?
        get() = shoppingItemsToolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(ShoppingItemOverviewViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_item_overview, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerAdapter = ShoppingItemOverviewAdapter(this::presentCreateEdit, checkedListener = { item ->
            viewModel.onItemChecked(item)
        })

        shopping_item_overview_recycler.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = recyclerAdapter
        }

        viewModel.listItems.observe(this, Observer { items ->
            items ?: return@Observer
            recyclerAdapter.items = items
        })
    }

    @OnClick(R.id.shopping_item_overview_fab)
    fun onCreateShoppingItemClicked() {
        presentCreateEdit()
    }

    private fun presentCreateEdit(id: String? = null) {
        val context = context ?: return
        val intent = CreateEditShoppingItemActivity.createIntent(context, id)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): ShoppingItemOverviewFragment = ShoppingItemOverviewFragment()
    }
}