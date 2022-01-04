package com.example.mainactivity.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.mainactivity.ChatsFragment
import com.example.mainactivity.R
import com.example.mainactivity.SearchFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseService : FirebaseMessagingService(){
    val CHANNEL_ID="my_channel_id"
    companion object{
        var sharedPref:SharedPreferences?= null
        var token:String?
        get(){
            return sharedPref?.getString("token","")
        }
        set(value) {
            sharedPref?.edit()?.putString("token",value)?.apply()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        token = p0
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val intent= Intent(this,ChatsFragment::class.java)
        val notificationMangager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId=Random.nextInt()

        createNotificationChannel(notificationMangager)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent= PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
        val notification=NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(p0.data["title"])
            .setContentTitle(p0.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_assistant_photo_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()


        notificationMangager.notify(notificationId,notification)
    }

    private fun createNotificationChannel(notificationMangager: NotificationManager) {

        val channelName="ChannelFirebaseChat"
        val channel=NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description="my firebase chat description"
            enableLights(true)
            lightColor=Color.GREEN
        }
        notificationMangager.createNotificationChannel(channel)
    }
}