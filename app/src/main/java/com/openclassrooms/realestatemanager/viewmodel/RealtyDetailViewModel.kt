package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.utils.NetworkSchedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 22/12/2021 12:05
 * Project : RealEstateManager
 **/
class RealtyDetailViewModel(private val database: AppDatabase, private val networkSchedulers: NetworkSchedulers) : ViewModel() {

    fun getRealtyData(realtyId: Int): Observable<Realty> =
        database.realtyDao()
            .getRealtyById(realtyId)
            .flatMap { realty ->
                getPictureById(realty.id).map { listOfPath ->
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
                        listOfPath
                    )
                }
            }
            .subscribeOn(networkSchedulers.io)
            .observeOn(networkSchedulers.main)


     fun getPictureById(id: Int): Observable<List<PicturesModel>> = database.pictureDao()
        .getPicturesById(id)
         .subscribeOn(networkSchedulers.io)
         .observeOn(networkSchedulers.main)

}
