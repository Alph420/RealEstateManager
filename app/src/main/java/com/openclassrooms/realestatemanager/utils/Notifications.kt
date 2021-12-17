package com.openclassrooms.realestatemanager.utils

import android.content.Context
import androidx.core.app.NotificationCompat
import com.openclassrooms.realestatemanager.R

/**
 * Created by Julien Jennequin on 17/12/2021 10:44
 * Project : RealEstateManager
 **/
class Notifications {

    companion object{
        private var CHANNEL_ID = "1"
    }

    fun notifyUserInsertSuccess(applicationContext: Context) {
        var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_add)
            .setContentTitle("Create realty")
            .setContentText("Your realty is save with success !")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
}