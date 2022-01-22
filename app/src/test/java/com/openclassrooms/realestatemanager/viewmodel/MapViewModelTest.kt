package com.openclassrooms.realestatemanager.viewmodel

import android.app.Instrumentation
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.GeoPoint
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.utils.TestNetworkSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


/**
 * Created by Julien Jennequin on 11/01/2022 14:37
 * Project : RealEstateManager
 **/
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {
    private val db: AppDatabase = Mockito.mock(AppDatabase::class.java)
    private val realtyDao = Mockito.mock(RealtyDao::class.java)
    private val networkSchedulers: TestNetworkSchedulers = TestNetworkSchedulers()
    private var viewmodel = MapViewModel(db, networkSchedulers)


    private var locationClient = Mockito.mock(FusedLocationProviderClient::class.java)
    private var task: Task<Location> = Mockito.mock(Task::class.java) as Task<Location>
    private var location = Mockito.mock(Location::class.java)
    private var lastLocation = Mockito.mock(Task::class.java) as Task<Location>

    @Before
    fun setup() {
        locationClient.setMockMode(true)
        locationClient.setMockLocation(location)
        Mockito.`when`(db.realtyDao()).thenReturn(realtyDao)


        Mockito.`when`(locationClient.lastLocation).thenReturn(lastLocation)
    }

    @Test
    //TODO FIX THIS TEST
    fun get_all_realty_test() {

        viewmodel.getAllRealty().test().assertValue {
            it.isEmpty()
        }
    }

    @Test
    //TODO FIX THIS TEST
    fun get_all_realty_test_error() {
        val expectedError = "error_test"

        viewmodel.getAllRealty().test().assertError {
            it.message == expectedError
        }
    }

    @Test
    //TODO FIX THIS TEST
    fun get_last_location_test() {
        Mockito.`when`(locationClient.lastLocation.addOnCompleteListener(any())).thenReturn(task)

        Mockito.`when`(locationClient.lastLocation.addOnCompleteListener(any())).thenAnswer {
            viewmodel.commeTuVeux(task)
        }
    }

}