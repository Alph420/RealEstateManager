package com.openclassrooms.realestatemanager.dao

import com.nhaarman.mockitokotlin2.any
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Julien Jennequin on 14/01/2022 12:43
 * Project : RealEstateManager
 **/

@RunWith(MockitoJUnitRunner::class)
class RealtyDaoTest {
    private val realtyDao: RealtyDao = Mockito.mock(RealtyDao::class.java)

    private val realty = RealtyModel(
        50, "", 500, 100, 1, 1, 1, "", "", "", "", "", "", 0.0, 0.0,
        "", true, 0, 0, ""
    )

    @Before
    fun setup() {
        Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.just(emptyList()))
        Mockito.`when`(realtyDao.getRealtyById(any())).thenReturn(Observable.just(realty))
        Mockito.`when`(realtyDao.insertRealty(any())).thenReturn(Single.just(1))
        Mockito.`when`(realtyDao.updateRealty(any())).thenReturn(Completable.complete())

    }

    @Test
    fun get_all_realty() {
        realtyDao.getAllRealty().test().await().assertComplete().assertValue(emptyList())
    }

    @Test
    fun get_realty_by_id() {
        realtyDao.getRealtyById("50").test().await().assertComplete().assertValue {
            it.id == realty.id
        }
    }

    @Test
    fun insert_realty() {
        realtyDao.insertRealty(realty).test().await().assertComplete().assertValue(1)
    }

    @Test
    fun update_realty() {
        realtyDao.updateRealty(realty).test().await().assertComplete()
    }
}