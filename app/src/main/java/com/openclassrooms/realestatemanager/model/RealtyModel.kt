package com.openclassrooms.realestatemanager.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.*
import com.google.firebase.firestore.GeoPoint
import java.sql.Date

/**
 * Created by Julien Jennequin on 10/12/2021 13:19
 * Project : RealEstateManager
 **/
@Entity
data class RealtyModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "kind") var kind: String,
    @ColumnInfo(name = "price") var price: Long = 0,
    @ColumnInfo(name = "area") var area: Long = 0,
    @ColumnInfo(name = "room_number") var roomNumber: Int,
    @ColumnInfo(name = "bathroom_number") var bathRoom: Int,
    @ColumnInfo(name = "bedroom_number") var bedRoom: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "longitude") var longitude: Double,
    @ColumnInfo(name = "latitude") var latitude: Double,
    @ColumnInfo(name = "point_of_interest") var pointOfInterest: String,
    @ColumnInfo(name = "available") var available: Boolean,
    @ColumnInfo(name = "in_market_date") var inMarketDate: Long,
    @ColumnInfo(name = "out_market_date") var outMarketDate: Long,
    @ColumnInfo(name = "estate_agent") var estateAgent: String,
    @ColumnInfo(name = "pictures") var pictures: ByteArray
    //, @ColumnInfo(name = "pictures")var pictures: PicturesModel

)

//TEST
data class RealtyAndPicture(
    @Embedded
    val realty: RealtyModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "realty"
    )
    val Media: List<RealtyModel>

)

