package com.openclassrooms.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.NetworkSchedulers
import io.reactivex.rxjava3.core.Observable
import org.osmdroid.util.GeoPoint

/**
 * Created by Julien Jennequin on 25/12/2021 10:51
 * Project : RealEstateManager
 **/
class MapViewModel(
    private val database: AppDatabase,
    private val networkSchedulers: NetworkSchedulers
) : ViewModel() {

    val mLocationLiveData = MutableLiveData<GeoPoint>()

    fun getAllRealty(): Observable<List<RealtyModel>> = database.realtyDao().getAllRealty()
        .subscribeOn(networkSchedulers.io)
        .observeOn(networkSchedulers.main)

    @SuppressLint("MissingPermission")
    fun getLastLocation(fusedLocationClient: FusedLocationProviderClient) {

        fusedLocationClient.lastLocation
            .addOnCompleteListener {
                commeTuVeux(it)
                /*             if (it.result != null) {
                                 Log.d(
                                     "MapViewModel",
                                     "Location user : ${it.result.latitude}, ${it.result.longitude} "
                                 )
                                 mLocationLiveData.postValue(GeoPoint(it.result.latitude, it.result.longitude))
                             } else {
                                 Log.d("MapViewModel", "Failed to get user position")

                             }*/
            }
            .addOnFailureListener {
                Log.d("MapViewModel", "Failed to get user position")
            }
    }

    //TODO rename
    fun commeTuVeux(taskLocation: Task<Location>) {
        if (taskLocation.result != null) {
            Log.d(
                "MapViewModel",
                "Location user : ${taskLocation.result.latitude}, ${taskLocation.result.longitude} "
            )
            mLocationLiveData.postValue(
                GeoPoint(
                    taskLocation.result.latitude,
                    taskLocation.result.longitude
                )
            )
        } else {
            Log.d("MapViewModel", "Failed to get user position")
        }
    }
}