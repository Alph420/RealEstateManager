package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.any
import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
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
class RealtyDetailViewModelTest {
    private val db: AppDatabase = Mockito.mock(AppDatabase::class.java)
    private val realtyDao = Mockito.mock(RealtyDao::class.java)
    private val pictureDao = Mockito.mock(PictureDao::class.java)
    private val networkSchedulers: TestNetworkSchedulers = TestNetworkSchedulers()
    private var viewmodel = RealtyDetailViewModel(db, networkSchedulers)
    var expectedId = 50

    val realtyModel = RealtyModel(
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
    val realty = Realty(
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
        emptyList(),
        true,
        0,
        0,
        "",
        emptyList()
    )
    private val pictureList = emptyList<PicturesModel>()

    @Before
    fun setup() {
        Mockito.`when`(db.realtyDao()).thenReturn(realtyDao)

    }

    @Test
    fun test_get_realty_data_by_id() {
        Mockito.`when`(realtyDao.getRealtyById(any())).thenReturn(
            Observable.just(
                realtyModel
            )
        )
        Mockito.`when`(db.pictureDao()).thenReturn(pictureDao)
        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))
        viewmodel.getRealtyById(expectedId).test().assertComplete().assertValue {
            it.id == realty.id
        }
    }

    @Test
    fun test_get_realty_data_by_id_error() {
        val expectedError = "error_test"
        Mockito.`when`(realtyDao.getRealtyById(any())).thenReturn(
            Observable.error(Throwable(expectedError))
        )

        viewmodel.getRealtyById(expectedId).test().assertError {
            it.message == expectedError
        }
    }
}