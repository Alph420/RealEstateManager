package com.openclassrooms.realestatemanager.model

/**
 * Created by Julien Jennequin on 02/01/2022 16:34
 * Project : RealEstateManager
 **/
data class FilterConstraint(
    var kind:String,
    var minPrice: Int = 0,
    var maxPrice:Int = 0,
    var minArea: Double = 0.0,
    var maxArea: Double = 0.0,
    var minRoom: Int,
    var maxRoom: Int,
    var minBathroom: Int,
    var maxBathroom: Int,
    var minBedroom: Int,
    var maxBedroom: Int,
    var pointOfInterest: String,
    var available: Boolean,
    )