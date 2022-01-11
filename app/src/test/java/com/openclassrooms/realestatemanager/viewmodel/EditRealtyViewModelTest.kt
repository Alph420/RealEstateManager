package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.any
import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Julien Jennequin on 11/01/2022 14:36
 * Project : RealEstateManager
 **/
@RunWith(MockitoJUnitRunner::class)
class EditRealtyViewModelTest {
    private val db: AppDatabase = Mockito.mock(AppDatabase::class.java)
    private val realtyDao = Mockito.mock(RealtyDao::class.java)
    private val pictureDao = Mockito.mock(PictureDao::class.java)
    private var viewmodel = EditRealtyViewModel(db)
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
    val pictureList = emptyList<PicturesModel>()

    @Before
    fun setup() {
        Mockito.`when`(db.realtyDao()).thenReturn(realtyDao)
        Mockito.`when`(realtyDao.getRealtyById(any())).thenReturn(
            Observable.just(
                realtyModel
            )
        )
        Mockito.`when`(realtyDao.updateRealty(any())).thenReturn(Completable.complete())

        Mockito.`when`(db.pictureDao()).thenReturn(pictureDao)
        Mockito.`when`(pictureDao.insertPictures(any())).thenReturn(Completable.complete())
        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

    }

    @Test
    fun test_get_realty_data_by_id() {
        viewmodel.getRealtyData("50").test().await().assertComplete().assertValue {
            it.id == realty.id
        }
    }

    @Test
    fun test_get_pictures_by_id() {
        viewmodel.getPictureById(50).test().await().assertComplete().assertValue(pictureList)
    }

    @Test
    fun test_update_realty() {
        viewmodel.updateRealty(realty, pictureList.toMutableList()).test().await().assertComplete()
    }

    @Test
    fun test_insert_pictures() {
        val pictureList = mutableListOf<PicturesModel>()
        viewmodel.insertPictures(realty,pictureList).test().await().assertComplete()
    }
}