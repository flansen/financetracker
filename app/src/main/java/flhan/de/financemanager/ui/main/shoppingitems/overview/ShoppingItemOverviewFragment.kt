package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import flhan.de.financemanager.ui.main.shoppingitems.createedit.CreateEditShoppingItemActivity
import kotlinx.android.synthetic.main.fragment_shopping_item_overview.*
import javax.inject.Inject

class ShoppingItemOverviewFragment : Fragment() {

    @Inject
    lateinit var factory: ShoppingItemOverviewViewModelFactory

    private lateinit var viewModel: ShoppingItemOverviewViewModel

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
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        ButterKnife.bind(this, view)
        return view
    }

    @OnClick(R.id.expense_overview_fab)
    fun onCreateExpenseClicked() {
        presentCreateEdit()
    }

    private fun presentCreateEdit(id: String? = null) {
        val context = context ?: return
        val intent = CreateEditShoppingItemActivity.createIntent(context, id)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): ShoppingItemOverviewFragment = ShoppingItemOverviewFragment()
        const val TAG = "shoppingItemOverviewFragment"
    }
}