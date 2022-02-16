package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.repository.RealtyRepository
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Julien Jennequin on 29/12/2021 18:55
 * Project : RealEstateManager
 **/
class SearchViewModel(private val repository: RealtyRepository) : ViewModel() {

    fun getAllRealty(): Observable<List<Realty>> =
        repository.getAllRealty()

    fun getFilteredRealty(
        filter: FilterConstraint,
        kind: String,
        city: String
    ): Observable<List<Realty>> =
        repository.getFilteredRealty(filter, kind, city)

}