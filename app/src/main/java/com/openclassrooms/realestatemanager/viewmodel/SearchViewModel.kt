package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 29/12/2021 18:55
 * Project : RealEstateManager
 **/
class SearchViewModel(private val database: AppDatabase) : ViewModel() {

    //TODO ADD PICTURE IN RX CHAIN
    fun getAll(): Observable<List<Realty>> =
        database.realtyDao()
            .getAllRealty()
            .subscribeOn(Schedulers.io())
            .map { realtyList ->
                realtyList.map { realty ->
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
                        realty.longitude,
                        realty.latitude,
                        realty.pointOfInterest,
                        realty.available,
                        realty.inMarketDate,
                        realty.outMarketDate,
                        realty.estateAgent,
                        emptyList()
                    )
                }
            }
            .observeOn(AndroidSchedulers.mainThread())


    fun getPictures(id: Int): Single<List<PicturesModel>> = database.pictureDao()
        .getPictures(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}