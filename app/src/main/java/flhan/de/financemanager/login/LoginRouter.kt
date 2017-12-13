package flhan.de.financemanager.login

import android.content.Context
import android.content.Intent
import flhan.de.financemanager.login.createjoinhousehold.CreateJoinHouseholdActivity

/**
 * Created by Florian on 29.09.2017.
 */
interface LoginRouter {
    fun navigateToCreateJoinHousehold()
}

class LoginRouterImpl(
        val context: Context
) : LoginRouter {

    override fun navigateToCreateJoinHousehold() {
        context.startActivity(Intent(context, CreateJoinHouseholdActivity::class.java))
    }
}