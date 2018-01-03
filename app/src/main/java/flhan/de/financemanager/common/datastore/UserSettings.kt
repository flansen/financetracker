package flhan.de.financemanager.common.datastore

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
        private const val USER_ID_KEY = "userId"
        private const val USER_ID_FALLBACK = ""
        private const val HOUSEHOLD_ID_KEY = "householdId"
        private const val HOUSEHOLD_ID_FALLBACK = ""
    }

    override fun getHouseholdId(): String {
        return sharedPreferences.getString(HOUSEHOLD_ID_KEY, HOUSEHOLD_ID_FALLBACK)
    }

    override fun setHouseholdId(id: String) {
        sharedPreferences.edit().putString(HOUSEHOLD_ID_KEY, id).commit()
    }

    override fun setUserId(id: String) {
        sharedPreferences.edit().putString(USER_ID_KEY, id).commit()
    }

    override fun getUserId(): String {
        return sharedPreferences.getString(USER_ID_KEY, USER_ID_FALLBACK)
    }

    override fun hasUserData(): Single<Boolean> {
        return Single.fromCallable<Boolean> {
            return@fromCallable getUserId().isNotBlank() && getHouseholdId().isNotBlank()
        }
    }
}