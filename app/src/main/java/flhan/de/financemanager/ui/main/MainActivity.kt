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
        mainBottombar.selectedItemId = R.id.tab_expenses
    }

    private fun handleBottomNavigationClicked(menuItem: MenuItem) {
        val tag = when (menuItem.itemId) {
            R.id.tab_expenses -> ExpenseOverviewFragment.TAG
            else -> PlaceholderFragment.TAG
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = when (menuItem.itemId) {
                R.id.tab_expenses -> ExpenseOverviewFragment.newInstance()
                else -> PlaceholderFragment()
            }
            fragmentTransaction.add(R.id.main_content_container, fragment, tag)
        } else {
            fragmentTransaction.attach(fragment)
        }

        val curFrag = supportFragmentManager.primaryNavigationFragment
        if (curFrag != null) {
            fragmentTransaction.detach(curFrag)
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNowAllowingStateLoss()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
}
