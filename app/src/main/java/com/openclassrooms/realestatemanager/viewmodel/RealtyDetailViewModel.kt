package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 22/12/2021 12:05
 * Project : RealEstateManager
 **/
class RealtyDetailViewModel(private val database: AppDatabase) : ViewModel() {


    fun getRealtyData(realtyId: String): Single<Realty> =
        database.realtyDao()
            .getById(realtyId)
            .subscribeOn(Schedulers.io())
            .flatMap { realty ->
                getPictures(realty.id).map { listOfPath ->
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
                        listOfPath
                    )
                }
            }

    private fun getPictures(id: Int): Single<List<String>> = database.pictureDao()
        .getPictures(id)
        .map { pictureList ->
            pictureList.map { picture ->
                picture.path
            }
        }
}


/*
item.forEach { picture ->
    // pathList.add(picture.path)
}*/
