package flhan.de.financemanager.common.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import flhan.de.financemanager.R
import flhan.de.financemanager.common.util.CurrencyString
import flhan.de.financemanager.di.ChannelId
import flhan.de.financemanager.ui.main.MainActivity
import javax.inject.Inject


class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    @Inject
    @field:ChannelId
    lateinit var channelId: String

    @Inject
    lateinit var notificationManager: NotificationManager


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        AndroidInjection.inject(this)

        val data = remoteMessage.data ?: return

        val amount = data[AMOUNT_KEY] ?: AMOUNT_FALLBACK
        val cause = data[CAUSE_KEY]
        val issuer = data[CREATOR_NAME_KEY]
        val currencyString = CurrencyString(amount).displayString

        val notificationTitle = "Neue Ausgabe"
        val notificationBody = "$issuer hat schon wieder $currencyString für $cause ausgegeben."

        sendNotification(notificationTitle, notificationBody)
    }


    private fun sendNotification(notificationTitle: String?, notificationBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody) as NotificationCompat.Builder

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        const val AMOUNT_KEY = "amount"
        const val CAUSE_KEY = "cause"
        const val CREATOR_NAME_KEY = "creatorName"
        const val AMOUNT_FALLBACK = "0"
    }
}
