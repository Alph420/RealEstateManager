package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.any
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.repository.RealtyRepository
import io.reactivex.rxjava3.core.Observable
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

    private val realtyRepository = Mockito.mock(RealtyRepository::class.java)
    private var viewmodel = SearchViewModel(realtyRepository)

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

    private val filter =
        FilterConstraint(
            "all", "all",
            0, 0, 0.0,
            0.0, 0,
            0, 0,
            0, 0,
            0, emptyList(),
            true, 0,
            0, 0,
            0, false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false
        )

    @Test
    fun get_all_realty_test() {
        Mockito.`when`(realtyRepository.getAllRealty()).thenReturn(Observable.just(emptyList()))

        viewmodel.getAllRealty().test().assertValue {
            it.isEmpty()
        }
    }

    @Test
    fun get_all_realty_test_error() {
        val expectedError = "error_test"

        Mockito.`when`(realtyRepository.getAllRealty())
            .thenReturn(Observable.error(Throwable(expectedError)))


        viewmodel.getAllRealty().test().assertError {
            it.message == expectedError
        }
    }

    @Test
    fun get_filtered_realty() {
        val realtyList =
            listOf(realty, realty.copy(id = 51), realty.copy(id = 52), realty.copy(id = 53))


        Mockito.`when`(realtyRepository.getFilteredRealty(any(), any(), any()))
            .thenReturn(Observable.just(realtyList))

        viewmodel.getFilteredRealty(filter, "all", "all").test().assertValue(realtyList)
    }

    @Test
    fun get_filtered_realty_error() {
        var expectedError = Throwable("error_test")

        Mockito.`when`(realtyRepository.getFilteredRealty(any(), any(), any()))
            .thenReturn(Observable.error(expectedError))

        viewmodel.getFilteredRealty(filter, "all", "all").test().assertError(expectedError)
    }
}