package com.openclassrooms.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Created by Julien Jennequin on 23/12/2021 12:39
 * Project : RealEstateManager
 **/

@Entity(
    foreignKeys = [ForeignKey(
        entity = RealtyModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("realty_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PicturesModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(index = true, name = "realty_id") val realtyId: Int,
    @ColumnInfo(name = "pictures") var path: String
)
