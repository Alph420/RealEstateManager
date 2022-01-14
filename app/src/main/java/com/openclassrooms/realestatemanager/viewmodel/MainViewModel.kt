package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.utils.NetworkSchedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java8.util.stream.Collectors
import java8.util.stream.StreamSupport

/**
 * Created by Julien Jennequin on 10/12/2021 13:56
 * Project : RealEstateManager
 **/
class MainViewModel(private val database: AppDatabase, private val networkSchedulers: NetworkSchedulers) : ViewModel() {

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

}