package com.openclassrooms.realestatemanager.viewmodel

import android.location.Geocoder
import com.nhaarman.mockitokotlin2.verify
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.util.Util
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Julien Jennequin on 13/01/2022 12:02
 * Project : RealEstateManager
 **/
@RunWith(MockitoJUnitRunner::class)
class UtilsTest {

    private val geoCoder = Mockito.mock(Geocoder::class.java)
    private var utils = Utils
    private val realtyModel = RealtyModel(
        50,
        "",
        500,
        100,
        1,
        1,
        1,
        "",
        "31 rue felix coquelle",
        "",
        "France",
        "Dunkerque",
        "59",
        0.0,
        0.0,
        "",
        true,
        0,
        0,
        ""
    )
    private val realty = realtyModel.toRealty(emptyList())

    @Test
    fun test_dollars_to_euros() {
        assert((50 * 0.812).roundToInt() == Utils.convertDollarToEuro(50))
    }

    @Test
    fun test_get_today_date() {
        assert(Utils.getTodayDate(SimpleDateFormat("dd/MM/yyyy").parse("10/01/2022").time) == "10/01/2022")
    }

    @Test
    fun test_euros_to_dollars() {
        assert((50 * 1.131).toInt() == Utils.convertEurosToDollars(50))
    }

    @Test
    fun test_format_price() {
        assert("1,000,000" == Utils.formatPrice(1000000))
        assert("100,000" == Utils.formatPrice(100000))
        assert("1,000" == Utils.formatPrice(1000))
    }

    @Test
    fun test_get_location_from_address_realty_model() {
        assert(realtyModel.address == utils.getLocationFromAddress(geoCoder, realtyModel).address)
    }

    @Test
    fun test_get_location_from_address_realty() {
        assert(realtyModel.address == utils.getLocationFromAddress(geoCoder, realtyModel).address)
    }

    @Test
    fun test_date_from_string() {
        assert(Utils.getDateFromString("10/01/2022") == SimpleDateFormat("dd/MM/yyyy").parse("10/01/2022").time)
    }
}