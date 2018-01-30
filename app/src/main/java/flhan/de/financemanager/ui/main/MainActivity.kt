package flhan.de.financemanager.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import com.google.firebase.messaging.FirebaseMessaging
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainBottombar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tab_expenses -> {
                    showTab(ExpenseOverviewFragment.newInstance())
                }
                else -> showTab(PlaceholderFragment())
            }
            true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(NotificationChannel("channelId",
                    "channelName", NotificationManager.IMPORTANCE_LOW))
        }
        mainBottombar.selectedItemId = R.id.tab_expenses
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications")
    }

    private fun showTab(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_content_container, fragment)
                .commit()
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
}
