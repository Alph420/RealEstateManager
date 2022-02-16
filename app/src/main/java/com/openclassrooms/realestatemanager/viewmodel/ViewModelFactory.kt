package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.repository.RealtyRepository
import com.openclassrooms.realestatemanager.utils.NetworkSchedulersImpl

/**
 * Created by Julien Jennequin on 11/12/2021 09:18
 * Project : RealEstateManager
 **/
class ViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(database, NetworkSchedulersImpl()) as T
        }
        if (modelClass.isAssignableFrom(AddRealtyViewModel::class.java)) {
            return AddRealtyViewModel(database, NetworkSchedulersImpl()) as T
        }
        if (modelClass.isAssignableFrom(RealtyDetailViewModel::class.java)) {
            return RealtyDetailViewModel(database, NetworkSchedulersImpl()) as T
        }
        if (modelClass.isAssignableFrom(EditRealtyViewModel::class.java)) {
            return EditRealtyViewModel(database, NetworkSchedulersImpl()) as T
        }
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(database, NetworkSchedulersImpl()) as T
        }
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                RealtyRepository(
                    database.realtyDao(),
                    database.pictureDao(),
                    NetworkSchedulersImpl()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}