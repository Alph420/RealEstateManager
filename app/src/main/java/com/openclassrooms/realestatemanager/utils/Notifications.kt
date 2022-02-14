package com.openclassrooms.realestatemanager.utils

import android.content.Context
import androidx.core.app.NotificationCompat

import android.app.NotificationManager

import android.app.NotificationChannel

import android.os.Build

import androidx.core.app.NotificationManagerCompat
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.RealtyModel


/**
 * Created by Julien Jennequin on 17/12/2021 10:44
 * Project : RealEstateManager
 **/
class Notifications {

    companion object {
        private var CHANNEL_ID: Int = 1
    }

    fun notifyUserInsertSuccess(applicationContext: Context, realty: RealtyModel) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID.toString(), "channel_name", importance)


            val notificationManager: NotificationManager =
                applicationContext.getSystemService(
                    NotificationManager::class.java
                )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID.toString())
                .setContentTitle(
                    applicationContext.getString(
                        R.string.notifications_title,
                        realty.kind
                    )
                )
                .setSmallIcon(R.drawable.ic_home)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            applicationContext.getString(
                                R.string.notification_content,
                                realty.estateAgent
                            )
                        )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1, builder.build())
    }
}