package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.NetworkSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java8.util.stream.Collectors
import java8.util.stream.StreamSupport

/**
 * Created by Julien Jennequin on 29/12/2021 18:55
 * Project : RealEstateManager
 **/
class SearchViewModel(
    private val database: AppDatabase, private val networkSchedulers: NetworkSchedulers
) : ViewModel() {

    fun getAllRealty(): Observable<List<Realty>> =
        database.realtyDao()
            .getAllRealty()
            .flatMap { realtyList ->
                val observableList = realtyList.map { realty ->
                    getPictureById(realty.id).map {
                        realty.toRealty(it)
                    }
                }
                if (observableList.isNotEmpty()) {
                    Observable.zip(observableList) { objects: Array<Any> ->
                        StreamSupport.stream(objects.toList())
                            .map { o -> o as Realty }
                            .collect(Collectors.toList())
                    }
                } else {
                    Observable.just(emptyList())
                }
            }
            .subscribeOn(networkSchedulers.io)
            .observeOn(networkSchedulers.main)

    fun getPictureById(id: Int): Observable<List<PicturesModel>> = database.pictureDao()
        .getPicturesById(id)
        .subscribeOn(networkSchedulers.io)

    fun realtyFilter(
        filter: FilterConstraint,
        kind: String,
        city: String
    ): Single<List<Realty>> =
        getAllRealty().map { listForLoop ->
            return@map listForLoop.filter { realty ->

                if (filter.kind != kind) {
                    if (realty.kind.lowercase() != filter.kind.lowercase()) {
                        return@filter false
                    }
                }

                if (filter.city != city) {
                    if (filter.city.lowercase() != realty.city.lowercase()) {
                        return@filter false
                    }
                }
                if (filter.filterCheckForPrice) {
                    if (realty.price < filter.minPrice) return@filter false
                    if (realty.price > filter.maxPrice) return@filter false
                }

                if (filter.filterCheckForArea) {
                    if (realty.area < filter.minArea) return@filter false
                    if (realty.area > filter.maxArea) return@filter false
                }

                if (filter.filterCheckForRoom) {
                    if (realty.roomNumber < filter.minRoom) return@filter false
                    if (realty.roomNumber > filter.maxRoom) return@filter false
                }

                if (filter.filterCheckForBathroom) {
                    if (realty.bathRoom < filter.minBathroom) return@filter false
                    if (realty.bathRoom > filter.maxBathroom) return@filter false
                }

                if (filter.filterCheckForBedroom) {
                    if (realty.bedRoom < filter.minBedroom) return@filter false
                    if (realty.bedRoom > filter.maxBedroom) return@filter false
                }

                if (filter.filterCheckForPictures) {
                    if (realty.pictures.size < filter.minPictures) return@filter false
                    if (realty.pictures.size > filter.maxPictures) return@filter false
                }

                if (!realty.pointOfInterest.containsAll(filter.pointOfInterest)) return@filter false

                if (filter.filterCheckForAvailability) {
                    if (realty.available != filter.available) return@filter false
                }

                if (filter.filterCheckInDate) {
                    if (realty.inMarketDate < filter.inMarketDate) return@filter false
                }

                if (filter.filterCheckOutDate) {
                    if (realty.outMarketDate > filter.outMarketDate) return@filter false
                }

                true
            }
        }.firstOrError()

}