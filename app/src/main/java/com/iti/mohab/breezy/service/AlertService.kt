package com.iti.mohab.breezy.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.provider.Settings;
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.iti.mohab.breezy.MainActivity
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.manger.AlertWindowManger
import com.iti.mohab.breezy.util.getIcon
import kotlin.CharSequence
import kotlin.Int


class AlertService : Service() {

    private val CHANNEL_ID = 14
    private val FOREGROUND_ID = 7
    private var notificationManager: NotificationManager? = null
    var alertWindowManger: AlertWindowManger? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val description = intent?.getStringExtra("description")
        val icon = intent?.getStringExtra("icon")
        notificationChannel()
        startForeground(FOREGROUND_ID, makeNotification(description!!, icon!!))
        //start window manger
        if (Settings.canDrawOverlays(this)) {
            alertWindowManger = AlertWindowManger(this, description, icon)
            alertWindowManger!!.setMyWindowManger()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun makeNotification(description: String, icon: String): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val bitmap = BitmapFactory.decodeResource(resources, getIcon(icon))

        return NotificationCompat.Builder(
            applicationContext, "$CHANNEL_ID"
        )
            .setSmallIcon(getIcon(icon))
            .setContentText(description)
            .setContentTitle("Weather Alarm")
            .setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText(description)
//            )
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setAutoCancel(true)
            .build()
    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                "$CHANNEL_ID",
                name, importance
            )
            channel.description = description
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}