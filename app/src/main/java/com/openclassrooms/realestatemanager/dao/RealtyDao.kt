package com.openclassrooms.realestatemanager.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Julien Jennequin on 10/12/2021 13:22
 * Project : RealEstateManager
 **/
@Dao
interface RealtyDao {

    @Query("SELECT * FROM RealtyModel")
    fun getAllRealty(): Observable<List<RealtyModel>>

    @Query("SELECT * FROM RealtyModel WHERE id = :id")
    fun getRealtyById(id: String): Observable<RealtyModel>

    @Insert
    fun insertRealty(realty: RealtyModel): Single<Long>

    @Update
    fun updateRealty(realty: RealtyModel): Completable

    @Delete
    fun delete(user: RealtyModel)
}