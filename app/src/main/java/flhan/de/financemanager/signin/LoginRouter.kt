package flhan.de.financemanager.signin

import android.content.Context
import android.content.Intent
import flhan.de.financemanager.createjoinhousehold.CreateJoinHouseholdActivity
import javax.inject.Inject

/**
 * Created by Florian on 29.09.2017.
 */
interface LoginRouter {
    fun navigateToCreateJoinHousehold()
}

class LoginRouterImpl
@Inject constructor(
        val context: Context
) : LoginRouter {

    override fun navigateToCreateJoinHousehold() {
        context.startActivity(Intent(context, CreateJoinHouseholdActivity::class.java))
    }
}