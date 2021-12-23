package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.database.AppDatabase
import java.lang.IllegalArgumentException

/**
 * Created by Julien Jennequin on 11/12/2021 09:18
 * Project : RealEstateManager
 **/
class ViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(database) as T
        }
        if(modelClass.isAssignableFrom(AddRealtyViewModel::class.java)){
            return AddRealtyViewModel(database) as T
        }
        if(modelClass.isAssignableFrom(RealtyDetailViewModel::class.java)){
            return RealtyDetailViewModel(database) as T
        }
        if (modelClass.isAssignableFrom(EditRealtyViewModel::class.java)){
            return EditRealtyViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}