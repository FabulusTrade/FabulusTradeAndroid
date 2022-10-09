package ru.fabulus.fabulustrade.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Spanned
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.fabulus.fabulustrade.R
import ru.fabulus.fabulustrade.mvp.model.messaging.IMessagingService
import ru.fabulus.fabulustrade.mvp.presenter.service.MessagingPresenter
import ru.fabulus.fabulustrade.ui.App
import ru.fabulus.fabulustrade.ui.activity.MainActivity

class MessagingService : FirebaseMessagingService(), IMessagingService {
    private val presenter = MessagingPresenter(this)

    override fun onCreate() {
        App.instance.appComponent.inject(presenter)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        presenter.messageReceived(message.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        presenter.updateToken(token)
    }

    override fun showNotificationFinancialOperation(
        title: Spanned,
        operationResultTitle: Spanned,
        body: String,
        id: Int
    ) {
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_app_launcher_foreground)
            .setColor(0x00BCC1)
            .setContentTitle(title)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine(operationResultTitle)
                    .addLine(body)
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fabulus Trade",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(id, notificationBuilder.build())
    }

    override fun showNotificationNewPost(
        title: String,
        message: String,
        idPost: Int,
        idNotification: Int
    ) {
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_app_launcher_foreground)
            .setColor(0x00BCC1)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fabulus Trade",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(idNotification, notificationBuilder.build())
    }

    override fun showNotificationNewComment(
        title: String,
        message: String,
        idPost: Int,
        idComment: Int, idNotification: Int
    ) {
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_app_launcher_foreground)
            .setColor(0x00BCC1)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fabulus Trade",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(idNotification, notificationBuilder.build())
    }

    override fun showNotificationNewAnswer(
        title: String,
        message: String,
        idPost: Int,
        idComment: Int, idNotification: Int
    ) {
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_app_launcher_foreground)
            .setColor(0x00BCC1)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fabulus Trade",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(idNotification, notificationBuilder.build())
    }
}