package com.openclassrooms.realestatemanager.model

/**
 * Created by Julien Jennequin on 25/12/2021 17:15
 * Project : RealEstateManager
 **/
data class Realty(
     val id: Int,
     var kind: String,
     var price: Long = 0,
     var area: Long = 0,
     var roomNumber: Int,
     var bathRoom: Int,
     var bedRoom: Int,
     var description: String,
     var address: String,
     var region: String,
     var country: String,
     var city: String,
     var department: String,
     var longitude: Double,
     var latitude: Double,
     var pointOfInterest: List<String>,
     var available: Boolean,
     var inMarketDate: Long,
     var outMarketDate: Long,
     var estateAgent: String,
     var pictures:List<PicturesModel>
)