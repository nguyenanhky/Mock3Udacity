package com.udacity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.utils.Constance


@SuppressLint("NotificationPermission")
fun NotificationManager.sendNotification(context: Context, id:Long, statusString:String, filename:String){


    val intent = Intent(context, DetailActivity::class.java)

    intent.putExtra(Constance.DOWNLOAD_ID_KEY, id)
    intent.putExtra(Constance.NOTIFICATION_ID_KEY, Constance.NOTIFICATION_ID)
    intent.putExtra(Constance.STATUS_KEY, statusString)
    intent.putExtra(Constance.FILE_NAME_KEY, filename)

    val pendingIntent = PendingIntent.getActivity(context,Constance.NOTIFICATION_ID,intent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context,Constance.CHANNEL_ID)
        .setContentTitle(context.getString(R.string.notification_title))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentText(context.getString(R.string.notification_description))
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            context.getString(R.string.notification_button),pendingIntent)
        .setAutoCancel(true)


    notify(Constance.NOTIFICATION_ID,builder.build())
}

fun NotificationManager.createNotificationChannel(channelId:String, channelName:String){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_LOW)

        createNotificationChannel(channel)
    }
}