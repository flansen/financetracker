package flhan.de.financemanager.common.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import flhan.de.financemanager.R
import flhan.de.financemanager.common.util.toCurrencyString
import flhan.de.financemanager.di.ChannelId
import flhan.de.financemanager.di.UserId
import flhan.de.financemanager.ui.main.MainActivity
import javax.inject.Inject

class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    @Inject
    @field:ChannelId
    lateinit var channelId: String

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    @field:UserId
    lateinit var userId: String

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        AndroidInjection.inject(this)

        val data = remoteMessage.data ?: return

        val amount = data[AMOUNT_KEY] ?: return
        val cause = data[CAUSE_KEY]
        val creatorName = data[CREATOR_NAME_KEY]
        val creatorId = data[CREATOR_ID]

        if (creatorId == userId) {
            return
        }

        val currencyString = amount.toCurrencyString()
        val notificationTitle = applicationContext.getString(R.string.notification_expense_title)
        val notificationBody = applicationContext.getString(R.string.notification_expense_body, creatorName, cause, currencyString)

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
        const val CREATOR_ID = "creatorId"
        const val CURRENCY_NUMBER_PATTERN = "###,##0.00"
        const val CURRENCY_PATTERN = "%s %s"
    }
}
