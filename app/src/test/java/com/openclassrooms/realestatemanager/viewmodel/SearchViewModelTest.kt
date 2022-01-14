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
    private var viewModel = SearchViewModel(db, networkSchedulers)
    private val pictureList = emptyList<PicturesModel>()
    private val filter = Mockito.mock(FilterConstraint::class.java)
    private val binding = Mockito.mock(ActivitySearchBinding::class.java)
    private val realty = RealtyModel(
        50, "", 500, 100, 1, 1, 1, "", "", "", "", "", "", 0.0, 0.0,
        "", true, 0, 0, ""
    )

    @Before
    fun setup() {
        Mockito.`when`(db.realtyDao()).thenReturn(realtyDao)
        Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.just(emptyList()))

        Mockito.`when`(db.pictureDao()).thenReturn(pictureDao)
        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))
    }

    @Test
    //TODO FIX THIS TEST
    fun get_all_realty_test() {
        viewModel.getAllRealty().test().assertValueCount(1)
    }

    @Test
    fun test_get_pictures_by_id() {
        viewModel.getPictureById(50).test().assertComplete().assertValue(pictureList)
    }

    @Test
    //TODO FIX THIS TEST
    fun test_filter() {

    }

}