package com.openclassrooms.realestatemanager.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.PicturesModel
import com.openclassrooms.realestatemanager.model.RealtyModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Julien Jennequin on 25/12/2021 12:33
 * Project : RealEstateManager
 **/
@Dao
interface PictureDao {

    @Query("SELECT * FROM PicturesModel WHERE realty_id = :id")
    fun getPictures(id: Int): Single<List<PicturesModel>>

    @Insert
    fun insertAll(picture: List<PicturesModel>): Completable
}