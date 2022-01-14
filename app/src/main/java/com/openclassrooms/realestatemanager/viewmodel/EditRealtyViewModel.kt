package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 23/12/2021 14:44
 * Project : RealEstateManager
 **/
class EditRealtyViewModel(private val database: AppDatabase) : ViewModel() {

    fun getRealtyData(realtyId: String): Observable<Realty> =
        database.realtyDao()
            .getRealtyById(realtyId)
            .subscribeOn(Schedulers.io())
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

    fun getPictureById(id: Int): Observable<List<PicturesModel>> =
        database.pictureDao()
            .getPicturesById(id)
            .subscribeOn(Schedulers.io())


    fun updateRealty(realty: Realty, picturesList: MutableList<PicturesModel>): Completable =
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
                    realty.region,
                    realty.country,
                    realty.city,
                    realty.department,
                    realty.longitude,
                    realty.latitude,
                    realty.pointOfInterest.toString().replace("[", "").replace("]", "").trim(),
                    realty.available,
                    realty.inMarketDate,
                    realty.outMarketDate,
                    realty.estateAgent
                )
            )
            .andThen(
                insertPictures(realty, picturesList)
            )
            .subscribeOn(Schedulers.io())


    fun insertPictures(
        realty: Realty,
        picturesList: MutableList<PicturesModel>
    ): Completable =
        database.pictureDao()
            .insertPictures(picturesList.map {
                PicturesModel(0, realty.id, it.name, it.path)
            })
            .subscribeOn(Schedulers.io())

}