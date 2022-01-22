package com.openclassrooms.realestatemanager.viewmodel

import android.util.Log
import com.nhaarman.mockitokotlin2.any
import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.NetworkSchedulers
import com.openclassrooms.realestatemanager.utils.TestNetworkSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

/**
 * Created by Julien Jennequin on 11/01/2022 14:38
 * Project : RealEstateManager
 **/
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {
    private val db: AppDatabase = Mockito.mock(AppDatabase::class.java)
    private val realtyDao = Mockito.mock(RealtyDao::class.java)
    private val pictureDao = Mockito.mock(PictureDao::class.java)
    private val networkSchedulers: TestNetworkSchedulers = TestNetworkSchedulers()
    private var viewmodel = SearchViewModel(db, networkSchedulers)
    private val pictureList = emptyList<PicturesModel>()

    private val realtyModel = RealtyModel(
        50,
        "",
        500,
        100,
        1,
        1,
        1,
        "",
        "",
        "",
        "",
        "",
        "",
        0.0,
        0.0,
        "",
        true,
        0,
        0,
        ""
    )
    private val realty = realtyModel.toRealty(emptyList())

    @Before
    fun setup() {
        Mockito.`when`(db.realtyDao()).thenReturn(realtyDao)
        Mockito.`when`(db.pictureDao()).thenReturn(pictureDao)
    }

    @Test
    //TODO I NEED TO PUSH ONE REALTY TO DB TO GET DATA BACK HERE ????
    fun get_all_realty_test() {
        Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.just(emptyList()))
        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

        viewmodel.getAllRealty().test().assertValue {
            it.isEmpty()
        }
    }

    @Test
    fun get_all_realty_test_error(){
        val expectedError = "error_test"

        Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.error(Throwable(expectedError)))
        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.error(Throwable(expectedError)))


        viewmodel.getAllRealty().test().assertError {
            it.message == expectedError
        }
    }

    @Test
    //TODO FIX THIS TEST
    //TODO ADD ERROR CASE
    //TODO ADD EMPTY CASE
    //TODO ADD EMPTY CASE
    fun test_filter() {
        //TODO
        // Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.just(listOf(realtyModel,realtyModel.copy(roomNumber = 5))))
        //viewModel.realtyFilter(filter, "all", "all").test().assertValue(emptyList())
    }

}