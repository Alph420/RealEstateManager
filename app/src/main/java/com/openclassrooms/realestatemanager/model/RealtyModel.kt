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
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "price") val price: Long = 0,
    @ColumnInfo(name = "area")val area:Long = 0,
    @ColumnInfo(name = "room_number")val roomNumber:Int,
    @ColumnInfo(name = "description")val description:String,
    @ColumnInfo(name = "address")val address:String,
    @ColumnInfo(name = "point_of_interest")val pointOfInterest:List<String>,
    @ColumnInfo(name = "available")val available:Boolean,
    @ColumnInfo(name = "in_market_date")val inMarketDate: Date,
    @ColumnInfo(name = "out_market_date")val outMarketDate:Date,
    @ColumnInfo(name = "estate_agent")val estateAgent:String,
    //TODO ADD A LIST OF IMAGES/PICTURE
    )

