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
        childColumns = arrayOf("realtyId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PicturesModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(index = true) val realtyId: String


    )
