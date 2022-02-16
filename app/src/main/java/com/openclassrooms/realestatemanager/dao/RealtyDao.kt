package com.openclassrooms.realestatemanager.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
    fun getRealtyById(id: Int): Observable<RealtyModel>

    @Insert
    fun insertRealty(realty: RealtyModel): Single<Long>

    @Update
    fun updateRealty(realty: RealtyModel): Completable

    @Query(
        """SELECT * FROM RealtyModel realty
            WHERE kind IN(:kind)
            AND city IN(:city)
            AND available IN(:available)
            AND area >= :minArea
            AND area <= :maxArea
            AND room_number >= :minRoom
            AND room_number <= :maxRoom
            AND bathroom_number >= :minBathroom
            AND bathroom_number <= :maxBathroom
            AND bedroom_number >= :minBedroom
            AND bedroom_number <= :maxBedroom
            AND in_market_date >= :inMarketDate
            AND out_market_date <= :outMarketDate
            AND price >= :minPrice
            AND price <= :maxPrice
            AND (SELECT COUNT(*) FROM PicturesModel WHERE realty_id = realty.id) >= :minPictures
            AND (SELECT COUNT(*) FROM PicturesModel WHERE realty_id = realty.id) <= :maxPictures
            COLLATE NOCASE
            """
    )

    fun getFilteredRealty(
        kind: List<String>,
        city: List<String>,
        minPrice: Int,
        maxPrice: Int,
        minArea: Double,
        maxArea: Double,
        minRoom: Int,
        maxRoom: Int,
        minBathroom: Int,
        maxBathroom: Int,
        minBedroom: Int,
        maxBedroom: Int,
        available: Array<Boolean>,
        inMarketDate: Long,
        outMarketDate: Long,
        minPictures: Int,
        maxPictures: Int
    ): Observable<List<RealtyModel>>


    @Query("SELECT kind from RealtyModel")
    fun getAllKinds(): Single<List<String>>

    @Query("SELECT city from RealtyModel")
    fun getAllCities(): Single<List<String>>

}