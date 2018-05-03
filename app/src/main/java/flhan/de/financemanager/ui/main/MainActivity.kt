package flhan.de.financemanager.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.ui.main.expenses.overview.ExpenseOverviewFragment
import flhan.de.financemanager.ui.main.shoppingitems.overview.ShoppingItemOverviewFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private var presentedId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFragmentAdapter()
        setupBottomNavigation(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        presentedId?.apply {
            outState?.putInt(SELECTED_ID_KEY, this)
        }
        super.onSaveInstanceState(outState)
    }

    private fun handleBottomNavigationClicked(menuItem: MenuItem) {
        val selectedIndex = when (menuItem.itemId) {
            R.id.tab_expenses -> 0
            R.id.tab_shopping_items -> 1
            else -> 2
        }
        main_content_container.currentItem = selectedIndex
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            mainBottombar.selectedItemId = R.id.tab_expenses
        } else if (savedInstanceState.containsKey(SELECTED_ID_KEY)) {
            presentedId = savedInstanceState[SELECTED_ID_KEY] as Int
        }

        mainBottombar.setOnNavigationItemSelectedListener { menuItem ->
            if (presentedId == menuItem.itemId) {
                return@setOnNavigationItemSelectedListener false
            }
            presentedId = menuItem.itemId
            handleBottomNavigationClicked(menuItem)
            true
        }
    }

    private fun setupFragmentAdapter() {
        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.apply {
            addFragment(ExpenseOverviewFragment.newInstance())
            addFragment(ShoppingItemOverviewFragment.newInstance())
            addFragment(PlaceholderFragment())
        }
        main_content_container.adapter = fragmentAdapter
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    companion object {
        const val SELECTED_ID_KEY = "selectedId"
    }
}
