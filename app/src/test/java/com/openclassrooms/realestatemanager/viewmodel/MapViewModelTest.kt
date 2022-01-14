package com.openclassrooms.realestatemanager.viewmodel

import android.app.Instrumentation
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.database.AppDatabase
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
    private var viewmodel = MapViewModel(db)

    @Before
    fun setup() {
        Mockito.`when`(db.realtyDao()).thenReturn(realtyDao)
        Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.just(emptyList()))
    }

    @Test
    fun get_all_realty_test() {
        viewmodel.getAllRealty().test().await().assertComplete().assertValue { it.isEmpty() }
    }


    @Test
    //TODO FIX THIS TEST
    fun get_last_location_test() {
    }
}