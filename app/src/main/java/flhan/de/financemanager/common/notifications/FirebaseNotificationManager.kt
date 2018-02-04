package flhan.de.financemanager.common.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import flhan.de.financemanager.di.ChannelId
import flhan.de.financemanager.di.ChannelName
import javax.inject.Inject

interface FirebaseNotificationManager {
    fun subscribe(householdId: String)
    fun unSubscribe(householdId: String)
}

class FirebaseNotificationManagerImpl @Inject constructor(
        private val notificationManager: NotificationManager,
        @ChannelName private val channelName: String,
        @ChannelId private val channelId: String
) : FirebaseNotificationManager {

    override fun subscribe(householdId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    NotificationChannel(channelId, channelName, IMPORTANCE_LOW)
            )
        }
        FirebaseMessaging.getInstance().subscribeToTopic(householdId)
    }

    override fun unSubscribe(householdId: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(householdId)
    }
}