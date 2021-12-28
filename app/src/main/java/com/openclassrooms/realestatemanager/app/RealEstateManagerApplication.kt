package com.openclassrooms.realestatemanager.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.openclassrooms.realestatemanager.utils.AndroidUtils

/**
 * Created by Julien Jennequin on 10/12/2021 12:39
 * Project : RealEstateManager
 **/
class RealEstateManagerApplication : MultiDexApplication() {

    private var context: Context? = null

    companion object {
        private const val TAG = "RealEstateManagerApplication"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        AndroidUtils.loadAppTheme()
    }
}