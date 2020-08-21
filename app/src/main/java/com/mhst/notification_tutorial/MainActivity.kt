package com.mhst.notification_tutorial

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val CHANNEL_ID = "My_Notificaion_Channel_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotiChannel()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("firebaseTag", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d("firebaseTag", token)
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })

        val notiId = 1
        val notiIdTwo = 2
        val notiIdThree = 3
        val notiIdFour = 4
        val notiIdFive = 5
        val notiIdSix = 6

        val notiTitle = "Hello my friend"
        val notiContent = "This is my noti content"

        var builder = NotificationCompat.Builder(this,CHANNEL_ID).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(notiTitle)
            setContentText(notiContent)
            setStyle(NotificationCompat.BigTextStyle().bigText("too large to show thanks"))
        }

        // noti with intent

        val intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pIntent = PendingIntent.getActivity(this,0,intent,0)

        val builder2 = builder.apply {
            setContentIntent(pIntent)
        }

        //noti with progress
        val builder4 = NotificationCompat.Builder(this,CHANNEL_ID).apply {
            setSmallIcon(android.R.drawable.stat_sys_download)
            setContentTitle("File download")
            setContentText("Download in Progress")
            setStyle(NotificationCompat.BigTextStyle().bigText("too large to show thanks"))
        }

        val pg_max = 100
        val pg_current = 10

        NotificationManagerCompat.from(this).apply {
            builder4.setProgress(pg_max,pg_current,false)
            builder.setContentText("Download Complete")
                .setProgress(0,0,false)
               btnNotiWithProgress.setOnClickListener {
                    notify(notiIdFour,builder4.build())
               }
        }

        //noti with image
         val drawable = ContextCompat.getDrawable(this,R.drawable.ic_launcher_background)
          val bitmap = drawable?.toBitmap()

        val builder5 = builder.apply {
            setLargeIcon(bitmap)
            setStyle(NotificationCompat.BigPictureStyle().bigLargeIcon(bitmap).bigPicture(bitmap))
        }

        btnFirstNoti.setOnClickListener {
            with(NotificationManagerCompat.from(this)){
                notify(notiId,builder.build())
            }
        }

        btnNotiWithIntent.setOnClickListener {
            with(NotificationManagerCompat.from(this)){
                notify(notiIdTwo,builder2.build())
            }
        }

        btnNotiWithImage.setOnClickListener {
            with(NotificationManagerCompat.from(this)){
                  notify(notiIdFive,builder5.build())
            }
        }


        //custom noti
        val customNotiLayout = RemoteViews(packageName, R.layout.custom_noti_layout)
        val customNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .apply {
                setSmallIcon(R.drawable.ic_launcher_background)
                setCustomBigContentView(customNotiLayout)
                setStyle(NotificationCompat.DecoratedCustomViewStyle())
            }

        btnCustomNoti.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                notify(notiIdSix, customNotification.build())
            }
        }

    }


    private fun createNotiChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "MHST"
            val text = "MHST's Noti Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                 description = text
            }
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}