package com.mhst.notification_tutorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by Moe Htet on 20,August,2020
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val body = message.data["body"]
        val title = message.data["title"]

        showNotication(title,body)

    }


    private fun showNotication(title : String?,body : String?){
        val channelId = "Channel_Id"
        val channelName = "Channel_Name"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pIntent = PendingIntent.getActivity(
            this,0,intent,PendingIntent.FLAG_ONE_SHOT
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            setupNotificationChannel(channelId,channelName,notificationManager)
        }

        val notiBuilder = NotificationCompat.Builder(this,channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(title)
            setContentText(body)
            setContentIntent(pIntent)
        }

        notificationManager.notify(0,notiBuilder.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setupNotificationChannel(id : String, name : String, notificationManager: NotificationManager){

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id,name,importance).apply {
                description = "Desc for channel"
            }
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

    }


}