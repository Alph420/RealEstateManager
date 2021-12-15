package com.openclassrooms.realestatemanager.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.openclassrooms.realestatemanager.app.RealEstateManagerApplication

/**
 * Created by Julien Jennequin on 10/12/2021 12:43
 * Project : RealEstateManager
 **/
object AndroidUtils {

    /**
     * Refresh the theme for the whole app
     */
    fun loadAppTheme() {
        when (PreferenceManager.getDefaultSharedPreferences(RealEstateManagerApplication().applicationContext).getString(
            "theme",
            "default"
        )) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}