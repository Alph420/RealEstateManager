package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 23/12/2021 14:44
 * Project : RealEstateManager
 **/
class EditRealtyViewModel(private val database: AppDatabase) : ViewModel() {

    fun getById(realtyId: String): Observable<RealtyModel> = database.realtyDao().getById(realtyId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun updateRealty(realty: RealtyModel): Completable = database.realtyDao().updateRealty(realty)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}