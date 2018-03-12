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
        mainBottombar.setOnNavigationItemSelectedListener { menuItem ->
            if (presentedId == menuItem.itemId) {
                return@setOnNavigationItemSelectedListener false
            }
            presentedId = menuItem.itemId
            handleBottomNavigationClicked(menuItem)
            true
        }
        if (savedInstanceState == null) {
            mainBottombar.selectedItemId = R.id.tab_expenses
        } else if (savedInstanceState.containsKey(SELECTED_ID_KEY)) {
            presentedId = savedInstanceState[SELECTED_ID_KEY] as Int
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        presentedId?.apply {
            outState?.putInt(SELECTED_ID_KEY, this)
        }
        super.onSaveInstanceState(outState)
    }

    private fun handleBottomNavigationClicked(menuItem: MenuItem) {
        val tag = when (menuItem.itemId) {
            R.id.tab_expenses -> ExpenseOverviewFragment.TAG
            R.id.tab_shopping_items -> ShoppingItemOverviewFragment.TAG
            else -> PlaceholderFragment.TAG
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = when (menuItem.itemId) {
                R.id.tab_expenses -> ExpenseOverviewFragment.newInstance()
                R.id.tab_shopping_items -> ShoppingItemOverviewFragment.newInstance()
                else -> PlaceholderFragment()
            }
            fragmentTransaction.add(R.id.main_content_container, fragment, tag)
        } else {
            fragmentTransaction.attach(fragment)
        }

        supportFragmentManager.primaryNavigationFragment?.apply {
            fragmentTransaction.detach(this)
        }

        fragmentTransaction.apply {
            setPrimaryNavigationFragment(fragment)
            setReorderingAllowed(true)
            commit()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    companion object {
        const val SELECTED_ID_KEY = "selectedId"
    }
}
