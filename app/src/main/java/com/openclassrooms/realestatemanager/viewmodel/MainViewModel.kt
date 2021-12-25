package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 10/12/2021 13:56
 * Project : RealEstateManager
 **/
class MainViewModel(private val database: AppDatabase) : ViewModel() {

    fun getAll(): Observable<List<RealtyModel>> = database.realtyDao().getAllRealty()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun insertAll(realty: RealtyModel) = database.realtyDao().insertRealty(realty)

    fun delete(realty: RealtyModel) = database.realtyDao().delete(realty)

}