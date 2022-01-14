package com.openclassrooms.realestatemanager.dao

import com.nhaarman.mockitokotlin2.any
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
 * Created by Julien Jennequin on 14/01/2022 12:29
 * Project : RealEstateManager
 **/

@RunWith(MockitoJUnitRunner::class)
class PictureDaoTest {

    private val pictureDao: PictureDao = Mockito.mock(PictureDao::class.java)

    private val pictureList = emptyList<PicturesModel>()

    @Before
    fun setup() {
        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))
        Mockito.`when`(pictureDao.insertPictures(any())).thenReturn(Completable.complete())

    }

    @Test
    fun test_get_pictures_by_id() {
        pictureDao.getPicturesById(50).test().await().assertComplete().assertValue(pictureList)
    }

    @Test
    fun test_insert_pictures() {
        pictureDao.insertPictures(emptyList()).test().await().assertComplete()
    }

}