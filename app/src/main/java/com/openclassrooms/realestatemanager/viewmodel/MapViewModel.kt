package com.openclassrooms.realestatemanager.viewmodel

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
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
    val mLocationErrorLiveData = MutableLiveData<Boolean>()

    fun getAllRealty(): Observable<List<RealtyModel>> = database.realtyDao().getAllRealty()
        .subscribeOn(networkSchedulers.io)
        .observeOn(networkSchedulers.main)

    @SuppressLint("MissingPermission")
    fun getLastLocation(fusedLocationClient: FusedLocationProviderClient) {
        val mLocationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }

        fusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()!!
        ).addOnFailureListener {
            Log.d("MapViewModel", "Failed to get user position")
            mLocationErrorLiveData.postValue(true)
        }
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            Log.d(
                "MapViewModel",
                "Location user : ${mLastLocation.latitude}, ${mLastLocation.longitude} "
            )
            mLocationLiveData.postValue(
                GeoPoint(
                    mLastLocation.latitude,
                    mLastLocation.longitude
                )
            )
        }
    }
}