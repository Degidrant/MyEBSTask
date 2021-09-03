package com.flexeiprata.androidmytaskapplication.firebase

import android.content.Intent
import android.util.Log
import com.flexeiprata.androidmytaskapplication.MainActivity
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.common.PRODUCT_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMyService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {
            val cID = message.data[PRODUCT_ID]
            val messageBody = message.notification?.body ?: ""
            val messageHeader = message.notification?.title ?: ""
            message.notification?.let {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(PRODUCT_ID, cID)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val channelId = getString(R.string.default_notification_channel_id)
                val ntCord = FirebaseNotificationBuilder(this)
                val pendingIntent = ntCord.wrapPendingIntent(intent)
                val defaultSoundUri = ntCord.getDefaultNotificationSound()
                val notification = ntCord.buildDefaultNotification(
                    messageHeader,
                    messageBody,
                    channelId,
                    defaultSoundUri,
                    pendingIntent,
                    R.drawable.ns_cart_empty
                )
                ntCord.lockNotificationChannel(ntCord.getMainChannel(channelId))
                ntCord.buildChannelAndNotify(notification, ntCord.getLockedChannel())
            }
        }
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d(LOG_DEBUG, "sendRegistrationTokenToServer($token)")
    }
}