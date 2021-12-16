package com.openclassrooms.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

/**
 * Created by Julien Jennequin on 10/12/2021 13:19
 * Project : RealEstateManager
 **/
@Entity
data class RealtyModel (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "price") val price: Long = 0,
    @ColumnInfo(name = "area")val area:Long = 0,
    @ColumnInfo(name = "room_number")val roomNumber:Int,
    @ColumnInfo(name = "description")val description:String,
    @ColumnInfo(name = "address")val address:String,
    @ColumnInfo(name = "point_of_interest") var pointOfInterest:String,
    @ColumnInfo(name = "available")val available:Boolean,
    @ColumnInfo(name = "in_market_date")val inMarketDate: Long,
    @ColumnInfo(name = "out_market_date")val outMarketDate:Long,
    @ColumnInfo(name = "estate_agent")val estateAgent:String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val pictures:ByteArray
    )

