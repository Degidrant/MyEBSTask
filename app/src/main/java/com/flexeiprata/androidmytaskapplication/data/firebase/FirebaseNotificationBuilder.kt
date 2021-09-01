package com.flexeiprata.androidmytaskapplication.data.firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat

class FirebaseNotificationBuilder(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var notificationChannel: NotificationChannel? = null

    fun getMainChannel(channelID: String) = NotificationChannel(
        channelID,
        channelID,
        NotificationManager.IMPORTANCE_DEFAULT
    )

    fun lockNotificationChannel(channel: NotificationChannel) {
        notificationChannel = channel
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun wrapPendingIntent(intent: Intent): PendingIntent = PendingIntent.getActivity(
        context, 100, intent,
        PendingIntent.FLAG_ONE_SHOT
    )

    fun getLockedChannel() = notificationChannel!!

    fun getDefaultNotificationSound(): Uri =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    fun buildDefaultNotification(
        title: String,
        message: String,
        channelID: String,
        sound: Uri,
        pendingIntent: PendingIntent,
        icon: Int
    ) =
        NotificationCompat.Builder(context, channelID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(sound)
            .setContentIntent(pendingIntent)
            .build()

    fun buildChannelAndNotify(
        notification: Notification,
        channel: NotificationChannel,
        notificationID: Int = 100
    ) {
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(notificationID, notification)
    }
}