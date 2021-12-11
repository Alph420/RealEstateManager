package com.openclassrooms.realestatemanager.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Julien Jennequin on 10/12/2021 13:22
 * Project : RealEstateManager
 **/
@Dao
interface RealtyDao {

    @Query("SELECT * FROM RealtyModel")
    fun getAll():Observable<List<RealtyModel>>

    /*@Query("SELECT * FROM RealtyModel WHERE uid IN (:realtyIds)")
    fun getById(realtyIds: IntArray): List<RealtyModel>*/

    @Insert
    fun insertAll(vararg realty: RealtyModel)

    @Delete
    fun delete(user: RealtyModel)
}