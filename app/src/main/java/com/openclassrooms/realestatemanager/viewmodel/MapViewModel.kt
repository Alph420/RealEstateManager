package com.openclassrooms.realestatemanager.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.osmdroid.util.GeoPoint

/**
 * Created by Julien Jennequin on 25/12/2021 10:51
 * Project : RealEstateManager
 **/
class MapViewModel(private val database: AppDatabase) : ViewModel() {

    val mLocationLiveData = MutableLiveData<GeoPoint>()

    fun getAllRealty(): Observable<List<RealtyModel>> = database.realtyDao().getAllRealty()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (context == null ||
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnCompleteListener {
                if (it.result != null) {
                    Log.d(
                        "MapViewModel",
                        "Location user : ${it.result.latitude}, ${it.result.longitude} "
                    )
                    mLocationLiveData.postValue(GeoPoint(it.result.latitude, it.result.longitude))
                } else {
                    Log.d("MapViewModel", "Failed to get user position")

                }
            }
            .addOnFailureListener {
                Log.d("MapViewModel", "Fasiled to get user position")
            }
    }
}