package flhan.de.financemanager.ui.main.shoppingitems.overview

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import dagger.android.support.AndroidSupportInjection
import flhan.de.financemanager.R
import javax.inject.Inject

class ShoppingItemOverviewFragment : Fragment() {

    @Inject
    lateinit var factory: ShoppingItemOverviewViewModelFactory

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

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
        return view
    }

    companion object {
        fun newInstance(): ShoppingItemOverviewFragment = ShoppingItemOverviewFragment()
        const val TAG = "shoppingItemOverviewFragment"
    }
}