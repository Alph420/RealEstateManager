package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.NetworkSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 23/12/2021 14:44
 * Project : RealEstateManager
 **/
class EditRealtyViewModel(
    private val database: AppDatabase, private val networkSchedulers: NetworkSchedulers
) : ViewModel() {

    fun getRealtyById(realtyId: Int): Observable<Realty> =
        database.realtyDao()
            .getRealtyById(realtyId)
            .flatMap { realtyModel ->
                getPictureById(realtyModel.id).map { listOfPath ->
                    realtyModel.toRealty(listOfPath)
                }
            }
            .subscribeOn(networkSchedulers.io)
            .observeOn(networkSchedulers.main)

    private fun getPictureById(id: Int): Observable<List<PicturesModel>> =
        database.pictureDao()
            .getPicturesById(id)
            .subscribeOn(networkSchedulers.io)
            .observeOn(networkSchedulers.main)


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
            .subscribeOn(networkSchedulers.io)
            .observeOn(networkSchedulers.main)


    private fun insertPictures(
        realty: Realty,
        picturesList: MutableList<PicturesModel>
    ): Completable =
        database.pictureDao()
             .insertPictures(picturesList.map {
                PicturesModel(0, realty.id, it.name, it.path)
            })
            .subscribeOn(networkSchedulers.io)

}