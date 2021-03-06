package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.any
import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.TestNetworkSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito


/**
 * Created by Julien Jennequin on 11/01/2022 11:07
 * Project : RealEstateManager
 **/

@RunWith(MockitoJUnitRunner::class)
class AddRealtyViewModelTest {
    private val db: AppDatabase = Mockito.mock(AppDatabase::class.java)
    private val realtyDao = Mockito.mock(RealtyDao::class.java)
    private val pictureDao = Mockito.mock(PictureDao::class.java)
    private val networkSchedulers: TestNetworkSchedulers = TestNetworkSchedulers()
    private var viewmodel = AddRealtyViewModel(db, networkSchedulers)


    private val realty = RealtyModel(
        50, "", 500, 100, 1, 1, 1, "", "", "", "", "", "", 0.0, 0.0,
        "", true, 0, 0, ""
    )

    @Before
    fun setup() {
        Mockito.`when`(db.realtyDao()).thenReturn(realtyDao)
        Mockito.`when`(db.pictureDao()).thenReturn(pictureDao)
    }

    @Test
    fun test_insert_realty() {
        Mockito.`when`(realtyDao.insertRealty(any())).thenReturn(Single.just(50))
        Mockito.`when`(pictureDao.insertPictures(any())).thenReturn(Completable.complete())

        viewmodel.insertRealty(realty, emptyList()).test().assertComplete()
    }

    @Test
    fun test_insert_realty_error() {
        val expectedError = "error_test"
        Mockito.`when`(realtyDao.insertRealty(any()))
            .thenReturn(Single.error(Throwable(expectedError)))

        viewmodel.insertRealty(realty, emptyList()).test().assertError {
            it.message == expectedError
        }
    }
}