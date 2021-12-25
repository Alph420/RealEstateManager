package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 17/12/2021 10:03
 * Project : RealEstateManager
 **/
class AddRealtyViewModel(private val database: AppDatabase) : ViewModel() {

    fun getAll(): Observable<List<RealtyModel>> = database.realtyDao().getAllRealty()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun insertRealty(
        realty: RealtyModel, pictureList: List<String>,
    ): Completable =
        database.realtyDao()
            .insertRealty(realty)
            .flatMapCompletable { realtyId ->
                insertPictures(pictureList.map { path ->
                    PicturesModel(0, realtyId.toInt(), path)
                })
            }
            .subscribeOn(Schedulers.io())

    fun delete(realty: RealtyModel) = database.realtyDao().delete(realty)

    fun insertPictures(picturesModel: List<PicturesModel>): Completable =
        database.pictureDao().insertAll(picturesModel)
            .subscribeOn(Schedulers.io())

}