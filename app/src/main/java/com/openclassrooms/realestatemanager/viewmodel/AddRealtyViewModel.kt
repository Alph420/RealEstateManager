package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Notifications
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 17/12/2021 10:03
 * Project : RealEstateManager
 **/
class AddRealtyViewModel(private val database: AppDatabase):ViewModel() {

    fun getAll(): Observable<List<RealtyModel>> = database.realtyDao().getAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun insertRealty(context: Context, realty: RealtyModel): Completable = database.realtyDao().insertAll(realty)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnComplete {
            Notifications().notifyUserInsertSuccess(context.applicationContext,realty)
        }


    fun delete(realty: RealtyModel) = database.realtyDao().delete(realty)

}