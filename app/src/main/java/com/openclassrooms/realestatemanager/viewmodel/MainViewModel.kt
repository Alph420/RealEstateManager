package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java8.util.stream.Collectors
import java8.util.stream.StreamSupport

/**
 * Created by Julien Jennequin on 10/12/2021 13:56
 * Project : RealEstateManager
 **/
class MainViewModel(private val database: AppDatabase) : ViewModel() {

    fun getAllRealty(): Observable<List<Realty>> =
        database.realtyDao()
            .getAllRealty()
            .subscribeOn(Schedulers.io())
            .flatMap { realtyList ->
                val observableList = realtyList.map { realty ->
                    getPictures(realty.id).map {
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

    private fun getPictures(id: Int): Observable<List<PicturesModel>> = database.pictureDao()
        .getPicturesById(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}