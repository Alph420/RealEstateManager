package com.openclassrooms.realestatemanager.viewmodel

import android.graphics.Picture
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

    private val picture1 = PicturesModel(1, 1, "name", "path")
    private val picture2 = PicturesModel(2, 2, "name", "path")
    private val realtyModel = RealtyModel(
        50,
        "home",
        0,
        0,
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
    fun get_all_realty_test() {
        Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.just(emptyList()))
        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

        viewmodel.getAllRealty().test().assertValue {
            it.isEmpty()
        }
    }

    @Test
    fun get_all_realty_test_error() {
        val expectedError = "error_test"

        Mockito.`when`(realtyDao.getAllRealty())
            .thenReturn(Observable.error(Throwable(expectedError)))
        Mockito.`when`(pictureDao.getPicturesById(any()))
            .thenReturn(Observable.error(Throwable(expectedError)))


        viewmodel.getAllRealty().test().assertError {
            it.message == expectedError
        }
    }

    @Test
    fun test_filter_price() {
        val filter =
            FilterConstraint(
                "all", "all",
                100, 1000, 0.0,
                0.0, 0,
                0, 0,
                0, 0,
                0, emptyList(),
                true, 0,
                0, 0,
                0, true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
            )
        Mockito.`when`(realtyDao.getAllRealty())
            .thenReturn(Observable.just(listOf(realtyModel, realtyModel.copy(price = 250))))

        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

        viewmodel.realtyFilter(filter, "all", "all").test().assertValue {
            it.size == 1
        }
    }

    @Test
    fun test_filter_area() {
        val filter =
            FilterConstraint(
                "all", "all",
                100, 1000, 150.0,
                300.0, 0,
                0, 0,
                0, 0,
                0, emptyList(),
                true, 0,
                0, 0,
                0, false,
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false
            )
        Mockito.`when`(realtyDao.getAllRealty())
            .thenReturn(Observable.just(listOf(realtyModel, realtyModel.copy(area = 250))))

        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

        viewmodel.realtyFilter(filter, "all", "all").test().assertValue {
            it.size == 1
        }
    }

    @Test
    fun test_filter_room() {
        val filter =
            FilterConstraint(
                "all", "all",
                0, 0, 0.0,
                0.0, 5,
                8, 0,
                0, 0,
                0, emptyList(),
                true, 0,
                0, 0,
                0, false,
                false,
                true,
                false,
                false,
                false,
                false,
                false,
                false
            )
        Mockito.`when`(realtyDao.getAllRealty())
            .thenReturn(Observable.just(listOf(realtyModel, realtyModel.copy(roomNumber = 7))))

        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

        viewmodel.realtyFilter(filter, "all", "all").test().assertValue {
            it.size == 1
        }
    }

    @Test
    fun test_filter_bathroom() {
        val filter =
            FilterConstraint(
                "all", "all",
                0, 0, 0.0,
                0.0, 0,
                0, 5,
                8, 0,
                0, emptyList(),
                true, 0,
                0, 0,
                0, false,
                false,
                false,
                true,
                false,
                false,
                false,
                false,
                false
            )
        Mockito.`when`(realtyDao.getAllRealty())
            .thenReturn(Observable.just(listOf(realtyModel, realtyModel.copy(bathRoom = 7))))

        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

        viewmodel.realtyFilter(filter, "all", "all").test().assertValue {
            it.size == 1
        }
    }

    @Test
    fun test_filter_bedroom() {
        val filter =
            FilterConstraint(
                "all", "all",
                0, 0, 0.0,
                0.0, 0,
                0, 0,
                0, 5,
                8, emptyList(),
                true, 0,
                0, 0,
                0, false,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                false
            )
        Mockito.`when`(realtyDao.getAllRealty())
            .thenReturn(Observable.just(listOf(realtyModel, realtyModel.copy(bedRoom = 7))))

        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

        viewmodel.realtyFilter(filter, "all", "all").test().assertValue {
            it.size == 1
            it[0].bedRoom == 7
        }
    }

    @Test
    //TODO HELP
    fun test_filter_pictures() {
        val filter =
            FilterConstraint(
                "all", "all",
                0, 0, 0.0,
                0.0, 0,
                0, 0,
                0, 0,
                0, listOf(picture1.path,picture2.path),
                true, 0,
                0, 1,
                5, false,
                false,
                false,
                false,
                false,
                true,
                false,
                false,
                false
            )
        Mockito.`when`(realtyDao.getAllRealty())
            .thenReturn(Observable.just(listOf(realtyModel)))

        Mockito.`when`(pictureDao.getPicturesById(any()))
            .thenReturn(Observable.just(listOf(picture1,picture2)))

        viewmodel.realtyFilter(filter, "all", "all").test().assertValue {
            it.size == 1
        }
    }

}