package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding
import com.openclassrooms.realestatemanager.model.FilterConstraint
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java8.util.stream.Collectors
import java8.util.stream.StreamSupport

/**
 * Created by Julien Jennequin on 29/12/2021 18:55
 * Project : RealEstateManager
 **/
class SearchViewModel(private val database: AppDatabase) : ViewModel() {

    fun getAllRealty(): Observable<List<Realty>> =
        database.realtyDao()
            .getAllRealty()
            .subscribeOn(Schedulers.io())
            .flatMap { realtyList ->
                val observableList = realtyList.map { realty ->
                    getPictureById(realty.id).map {
                        Realty(
                            realty.id,
                            realty.kind,
                            realty.price,
                            realty.area,
                            realty.roomNumber,
                            realty.bathRoom,
                            realty.bedRoom,
                            realty.description,
                            realty.address,
                            realty.region,
                            realty.country,
                            realty.city,
                            realty.department,
                            realty.longitude,
                            realty.latitude,
                            realty.pointOfInterest.split(", "),
                            realty.available,
                            realty.inMarketDate,
                            realty.outMarketDate,
                            realty.estateAgent,
                            it
                        )
                    }
                }
                Observable.zip(observableList) { objects: Array<Any> ->
                    StreamSupport.stream(objects.toList())
                        .map { o -> o as Realty }
                        .collect(Collectors.toList())
                }
            }

     fun getPictureById(id: Int): Observable<List<PicturesModel>> = database.pictureDao()
        .getPicturesById(id)
        .subscribeOn(Schedulers.io())

    fun filter(
        filter: FilterConstraint,
        binding: ActivitySearchBinding,
        context: Context
    ): Single<List<Realty>> =
        getAllRealty().map { listForLoop ->
            return@map listForLoop.filter { realty ->

                if (filter.kind != context.getString(R.string.search_all_kind)) {
                    if (realty.kind.lowercase() != filter.kind.lowercase()) {
                        return@filter false
                    }
                }

                if (filter.city != context.getString(R.string.search_all_city)) {
                    if (filter.city.lowercase() != realty.city.lowercase()) {
                        return@filter false
                    }
                }
                if (filter.city != context.getString(R.string.search_all_city)) {
                    if (filter.city.lowercase() != realty.city.lowercase()) return@filter false
                }

                if (binding.include.filterCheckForPrice.isChecked) {
                    if (realty.price < filter.minPrice) return@filter false
                    if (realty.price > filter.maxPrice) return@filter false
                }

                if (binding.include.filterCheckForArea.isChecked) {
                    if (realty.area < filter.minArea) return@filter false
                    if (realty.area > filter.maxArea) return@filter false
                }

                if (binding.include.filterCheckForRoom.isChecked) {
                    if (realty.roomNumber < filter.minRoom) return@filter false
                    if (realty.roomNumber > filter.maxRoom) return@filter false
                }

                if (binding.include.filterCheckForBathroom.isChecked) {
                    if (realty.bathRoom < filter.minBathroom) return@filter false
                    if (realty.bathRoom > filter.maxBathroom) return@filter false
                }

                if (binding.include.filterCheckForBedroom.isChecked) {
                    if (realty.bedRoom < filter.minBedroom) return@filter false
                    if (realty.bedRoom > filter.maxBedroom) return@filter false
                }

                if (binding.include.checkFilterForPictures.isChecked) {
                    if (realty.pictures.size < filter.minPictures) return@filter false
                    if (realty.pictures.size > filter.maxPictures) return@filter false
                }

                if (!realty.pointOfInterest.containsAll(filter.pointOfInterest)) return@filter false

                if (binding.include.filterCheckForAvailability.isChecked) {
                    if (realty.available != filter.available) return@filter false
                }

                if (binding.include.filterInDate.text.isNotEmpty()) {
                    if (realty.inMarketDate < filter.inMarketDate) return@filter false
                }

                if (binding.include.filterOutDate.text.isNotEmpty()) {
                    if (realty.outMarketDate > filter.outMarketDate) return@filter false
                }

                true
            }
        }.firstOrError()

}