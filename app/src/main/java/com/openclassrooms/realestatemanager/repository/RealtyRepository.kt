package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.model.RealtyModel

/**
 * Created by Julien Jennequin on 11/12/2021 09:24
 * Project : RealEstateManager
 **/
class RealtyRepository(private val realtyDao: RealtyDao) {

    public fun getAll(){
        realtyDao.getAll()
    }

    public fun insertAll(realty: RealtyModel){
        realtyDao.insertAll(realty)
    }

    public fun delete(realty: RealtyModel){
        realtyDao.delete(realty)
    }

}