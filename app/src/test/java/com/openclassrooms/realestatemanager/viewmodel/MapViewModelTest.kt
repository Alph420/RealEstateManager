package com.openclassrooms.realestatemanager.viewmodel

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.nhaarman.mockitokotlin2.any
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.utils.TestNetworkSchedulers
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
    fun get_all_realty_test() {
        Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.just(emptyList()))

        viewmodel.getAllRealty().test().assertValue {
            it.isEmpty()
        }
    }

    @Test
    fun get_all_realty_test_error() {
        val expectedError = "error_test"

        Mockito.`when`(realtyDao.getAllRealty())
            .thenReturn(Observable.error(Throwable(expectedError)))

        viewmodel.getAllRealty().test().assertError {
            it.message == expectedError
        }
    }

    @Test
    //TODO VERIFY
    fun get_last_location_test() {
        Mockito.`when`(locationClient.lastLocation.addOnCompleteListener(any())).thenReturn(task)

        Mockito.`when`(locationClient.lastLocation.addOnCompleteListener(any())).thenAnswer {
            viewmodel.locationSuccess(task)
        }
    }

}