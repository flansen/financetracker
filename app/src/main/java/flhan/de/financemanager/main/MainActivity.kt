package flhan.de.financemanager.main

import android.os.Bundle
import android.support.v4.app.Fragment
import flhan.de.financemanager.R
import flhan.de.financemanager.base.BaseActivity
import flhan.de.financemanager.main.expenseoverview.ExpenseOverviewFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_bottombar.setOnTabSelectListener { selectedTabId ->
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

        main_bottombar.onSaveInstanceState()
    }


}
