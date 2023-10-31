package com.example.cupidswap.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.cupidswap.R
import com.example.cupidswap.Screens.ChatActivity
import com.example.cupidswap.Screens.Fragments.MessageFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

// this is for reciving notifications
class FirebaseMsgService : FirebaseMessagingService(){
    private val channelId = "cupidSwap"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this, MessageFragment::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createNotification(manager as NotificationManager)

        val intent2 = PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.notification)
            .setAutoCancel(true)
            .setContentIntent(intent2)
            .build()

        manager.notify(Random.nextInt(), notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(manager: NotificationManager){
        val channel = NotificationChannel(channelId, "cupidSwapChat", NotificationManager.IMPORTANCE_HIGH)
        channel.description = "New Chat"
        channel.enableLights(true)
        manager.createNotificationChannel(channel)
    }


}