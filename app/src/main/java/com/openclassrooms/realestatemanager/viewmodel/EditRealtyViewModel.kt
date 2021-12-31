package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 23/12/2021 14:44
 * Project : RealEstateManager
 **/
class EditRealtyViewModel(private val database: AppDatabase) : ViewModel() {

    fun getRealtyData(realtyId: String): Observable<Realty> =
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
            .observeOn(AndroidSchedulers.mainThread())

    private fun getPictures(id: Int): Observable<List<PicturesModel>> =
        database.pictureDao()
            .getPictures(id)
            .subscribeOn(Schedulers.io())

    fun updateRealty(realty: Realty): Completable =
        database.realtyDao()
            .updateRealty(
                RealtyModel(
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
                    realty.estateAgent
                )
            )
            .subscribeOn(Schedulers.io())



     fun insertPictures(realty: Realty, picturesList: MutableList<PicturesModel>): Completable =
        database.pictureDao()
            .insertAll(picturesList.map {
                PicturesModel(0,realty.id,it.name,it.path)
            })
            .subscribeOn(Schedulers.io())

}