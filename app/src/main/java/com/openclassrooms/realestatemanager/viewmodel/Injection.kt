package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import com.openclassrooms.realestatemanager.database.AppDatabase

/**
 * Created by Julien Jennequin on 11/12/2021 09:23
 * Project : RealEstateManager
 **/
class Injection {

    companion object{
        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val database = AppDatabase.getInstance(context)
            return ViewModelFactory(database)
        }
    }
}