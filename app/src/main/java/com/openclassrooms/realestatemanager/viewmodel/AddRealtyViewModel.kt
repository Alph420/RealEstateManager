package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.NetworkSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 17/12/2021 10:03
 * Project : RealEstateManager
 **/
class AddRealtyViewModel(
    private val database: AppDatabase,
    private val networkSchedulers: NetworkSchedulers
) : ViewModel() {

    fun insertRealty(
        realty: RealtyModel, pictureList: List<PicturesModel>,
    ): Completable =
        database.realtyDao()
            .insertRealty(realty)
            .flatMapCompletable { realtyId ->
                insertPictures(pictureList.map { picture ->
                    PicturesModel(0, realtyId.toInt(), picture.name, picture.path)
                })
            }
            .subscribeOn(networkSchedulers.io)
            .observeOn(networkSchedulers.main)


    private fun insertPictures(picturesModel: List<PicturesModel>): Completable =
        database.pictureDao().insertPictures(picturesModel)
            .subscribeOn(networkSchedulers.io)
            .observeOn(networkSchedulers.main)

}