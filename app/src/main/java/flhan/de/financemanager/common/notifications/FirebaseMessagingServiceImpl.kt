package flhan.de.financemanager.common.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import flhan.de.financemanager.R
import flhan.de.financemanager.common.util.CurrencyString
import flhan.de.financemanager.ui.main.MainActivity


class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var notificationTitle: String? = null
        var notificationBody: String? = null

        val amount = remoteMessage.data["amount"]
        val cause = remoteMessage.data["cause"]
        val issuer = remoteMessage.data["creatorName"]

        val currencyString = CurrencyString(amount ?: "0").displayString

        // Check if message contains a notification payload.
        if (remoteMessage.data != null) {
            notificationTitle = "Neue Ausgabe"
            notificationBody = "$issuer hat schon wieder $currencyString für $cause ausgegeben."
        }

        sendNotification(notificationTitle, notificationBody)
    }


    private fun sendNotification(notificationTitle: String?, notificationBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this, "channelId")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody) as NotificationCompat.Builder


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {

        private val TAG = "FirebaseMessagingServce"
    }
}
