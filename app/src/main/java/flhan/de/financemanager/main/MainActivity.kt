package flhan.de.financemanager.main

import android.os.Bundle
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.main.expenses.overview.ExpenseOverviewFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mainBottombar.setOnTabSelectListener { selectedTabId ->
            when(selectedTabId) {
                R.id.tab_expenses -> {
                    showTab(ExpenseOverviewFragment.newInstance())
                }
            }
        }
    }

    private fun showTab(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_content_container, fragment)
                .commit()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mainBottombar.onSaveInstanceState()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
}
