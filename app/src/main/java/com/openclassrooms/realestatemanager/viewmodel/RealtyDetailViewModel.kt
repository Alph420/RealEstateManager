package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Julien Jennequin on 22/12/2021 12:05
 * Project : RealEstateManager
 **/
class RealtyDetailViewModel(private val database: AppDatabase) : ViewModel() {

    fun getById(realtyId: String): Observable<RealtyModel> = database.realtyDao().getById(realtyId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}