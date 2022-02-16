package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.NetworkSchedulers
import io.reactivex.rxjava3.core.Observable
import java8.util.stream.Collectors
import java8.util.stream.StreamSupport

/**
 * Created by Julien Jennequin on 16/02/2022 12:28
 * Project : RealEstateManager
 **/
class RealtyRepository(
    private val realtyDao: RealtyDao,
    private val pictureDao: PictureDao,
    private val networkSchedulers: NetworkSchedulers
) {

    fun getAllRealty(): Observable<List<Realty>> {
        return realtyDao.getAllRealty().toRealty().subscribeOn(networkSchedulers.io)
    }

    fun getFilteredRealty(
        filter: FilterConstraint,
        kind: String,
        city: String
    ): Observable<List<Realty>> {
        return realtyDao.getAllKinds().flatMapObservable { kindList ->

            realtyDao.getAllCities().flatMapObservable { citiesList ->

                realtyDao.getFilteredRealty(
                    if (filter.kind == kind) kindList else listOf(filter.kind),
                    if (filter.city == city) citiesList else listOf(filter.city),
                    if (filter.filterCheckForPrice) filter.minPrice else Integer.MIN_VALUE,
                    if (filter.filterCheckForPrice) filter.maxPrice else Integer.MAX_VALUE,
                    if (filter.filterCheckForArea) filter.minArea else Double.MIN_VALUE,
                    if (filter.filterCheckForArea) filter.maxArea else Double.MAX_VALUE,
                    if (filter.filterCheckForRoom) filter.minRoom else Integer.MIN_VALUE,
                    if (filter.filterCheckForRoom) filter.maxRoom else Integer.MAX_VALUE,
                    if (filter.filterCheckForBathroom) filter.minBathroom else Integer.MIN_VALUE,
                    if (filter.filterCheckForBathroom) filter.maxBathroom else Integer.MAX_VALUE,
                    if (filter.filterCheckForBedroom) filter.minBedroom else Integer.MIN_VALUE,
                    if (filter.filterCheckForBedroom) filter.maxBedroom else Integer.MAX_VALUE,
                    if (filter.filterCheckForAvailability) arrayOf(filter.available) else arrayOf(
                        true,
                        false
                    ),
                    if (filter.filterCheckInDate) filter.inMarketDate else Long.MIN_VALUE,
                    if (filter.filterCheckOutDate) filter.outMarketDate else Long.MAX_VALUE,
                    if (filter.filterCheckForPictures) filter.minPictures else Integer.MIN_VALUE,
                    if (filter.filterCheckForPictures) filter.maxPictures else Integer.MAX_VALUE,
                )
            }
        }.toRealty()
            .subscribeOn(networkSchedulers.io)
    }


    private fun Observable<List<RealtyModel>>.toRealty(): Observable<List<Realty>> {
        return flatMap { realtyList ->
            val observableList = realtyList.map { realty ->
                pictureDao.getPicturesById(realty.id).map {
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
    }

}