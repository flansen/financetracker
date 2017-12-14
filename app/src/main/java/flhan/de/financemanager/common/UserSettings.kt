package flhan.de.financemanager.common

import android.content.SharedPreferences
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Florian on 03.10.2017.
 */
interface UserSettings {
    fun getUserId(): String
    fun setUserId(id: String)
    fun setHouseholdId(id: String)
    fun getHouseholdId(): String
    fun hasUserData(): Single<Boolean>
}

class UserSettingsImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : UserSettings {

    companion object {
        const val USER_ID_KEY = "userId"
        const val HOUSEHOLD_ID_KEY = "householdId"
    }

    override fun getHouseholdId(): String {
        return sharedPreferences.getString(HOUSEHOLD_ID_KEY, "")
    }

    override fun setHouseholdId(id: String) {
        sharedPreferences.edit().putString(HOUSEHOLD_ID_KEY, id).commit()
    }

    override fun setUserId(id: String) {
        sharedPreferences.edit().putString(USER_ID_KEY, id).commit()
    }

    override fun getUserId(): String {
        return sharedPreferences.getString(USER_ID_KEY, "")
    }

    override fun hasUserData(): Single<Boolean> {
        return Single.fromCallable<Boolean> {
            return@fromCallable getUserId().isNotBlank() && getHouseholdId().isNotBlank()
        }
    }
}