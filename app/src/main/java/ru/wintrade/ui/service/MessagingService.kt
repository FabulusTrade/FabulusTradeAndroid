package ru.wintrade.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.wintrade.R
import ru.wintrade.mvp.model.messaging.IMessagingService
import ru.wintrade.mvp.presenter.service.MessagingPresenter
import ru.wintrade.ui.App
import ru.wintrade.ui.activity.MainActivity
import java.util.concurrent.atomic.AtomicInteger

class MessagingService : FirebaseMessagingService(), IMessagingService {


    private val presenter = MessagingPresenter(this)

    override fun onCreate() {
        App.instance.appComponent.inject(presenter)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        presenter.messageReceived(message.data)
    }

    override fun onNewToken(p0: String) {

    }

    override fun showNotification(title: String, body: String, id: Int) {
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Win Trade",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notificationBuilder.build())
    }
}