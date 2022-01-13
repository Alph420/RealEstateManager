package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.any
import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.PicturesModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Julien Jennequin on 11/01/2022 14:38
 * Project : RealEstateManager
 **/
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {
    private val db: AppDatabase = Mockito.mock(AppDatabase::class.java)
    private val realtyDao = Mockito.mock(RealtyDao::class.java)
    private val pictureDao = Mockito.mock(PictureDao::class.java)
    private var viewModel = SearchViewModel(db)
    private val pictureList = emptyList<PicturesModel>()
    private val filter = Mockito.mock(FilterConstraint::class.java)
    private val binding = Mockito.mock(ActivitySearchBinding::class.java)


    @Before
    fun setup() {
        Mockito.`when`(db.realtyDao()).thenReturn(realtyDao)
        Mockito.`when`(realtyDao.getAllRealty()).thenReturn(Observable.just(emptyList()))

        Mockito.`when`(db.pictureDao()).thenReturn(pictureDao)
        Mockito.`when`(pictureDao.getPicturesById(any())).thenReturn(Observable.just(emptyList()))

        Mockito.`when`(viewModel.filter(any(), any(), any())).thenReturn(Single.just(emptyList()))

    }

    @Test
    //TODO FIX THIS TEST
    fun get_all_realty_test() {
        viewModel.getAllRealty().test().await().assertComplete().assertValue { it.isEmpty() }
    }

    @Test
    fun test_get_pictures_by_id() {
        viewModel.getPictureById(50).test().await().assertComplete().assertValue(pictureList)
    }

    @Test
    //TODO FIX THIS TEST
    fun test_filter() {
       /* viewModel.filter(filter, binding, this).test().await().assertComplete()
            .assertValue(emptyList())*/
    }

}