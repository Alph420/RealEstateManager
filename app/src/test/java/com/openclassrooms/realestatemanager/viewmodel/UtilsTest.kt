package com.openclassrooms.realestatemanager.viewmodel

import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.openclassrooms.realestatemanager.model.RealtyModel
import com.openclassrooms.realestatemanager.utils.Utils
import io.reactivex.rxjava3.core.Observable
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
    private val address = Mockito.mock(Address::class.java)
    private val connectivityManager = Mockito.mock(ConnectivityManager::class.java)
    private val networkCapabilities = Mockito.mock(NetworkCapabilities::class.java)
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
        "",
        "",
        "",
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
    fun test_internet_available_wifi() {
        val nw = connectivityManager.activeNetwork

        Mockito.`when`(connectivityManager.getNetworkCapabilities(nw))
            .thenReturn(networkCapabilities)


        val actNw = connectivityManager.getNetworkCapabilities(nw)

        Mockito.`when`(actNw!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            .thenReturn(true)

        assert(actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
    }

    @Test
    fun test_internet_available_cellular() {
        val nw = connectivityManager.activeNetwork

        Mockito.`when`(connectivityManager.getNetworkCapabilities(nw))
            .thenReturn(networkCapabilities)


        val actNw = connectivityManager.getNetworkCapabilities(nw)

        Mockito.`when`(actNw!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            .thenReturn(true)

        assert(actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    @Test
    fun test_internet_available_ethernet() {
        val nw = connectivityManager.activeNetwork

        Mockito.`when`(connectivityManager.getNetworkCapabilities(nw))
            .thenReturn(networkCapabilities)


        val actNw = connectivityManager.getNetworkCapabilities(nw)

        Mockito.`when`(actNw!!.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            .thenReturn(true)

        assert(actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    @Test
    fun test_internet_available_bluetooth() {
        val nw = connectivityManager.activeNetwork

        Mockito.`when`(connectivityManager.getNetworkCapabilities(nw))
            .thenReturn(networkCapabilities)


        val actNw = connectivityManager.getNetworkCapabilities(nw)

        Mockito.`when`(actNw!!.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))
            .thenReturn(true)

        assert(actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))
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
        Mockito.`when`(geoCoder.getFromLocationName(any(), any())).thenReturn(listOf(address))

        Mockito.`when`(address.adminArea).thenReturn("Nord")
        Mockito.`when`(address.countryName).thenReturn("France")
        Mockito.`when`(address.locality).thenReturn("59")
        Mockito.`when`(address.subAdminArea).thenReturn("Nord-pas-de-calais")
        Mockito.`when`(address.longitude).thenReturn(0.0)
        Mockito.`when`(address.latitude).thenReturn(0.0)

        var test = utils.getLocationFromAddress(geoCoder, realtyModel)

        assert("Nord" == test.region)
        assert("France" == test.country)
        assert("59" == test.city)
        assert("Nord-pas-de-calais" == test.department)
        assert(0.0 == test.longitude)
        assert(0.0 == test.latitude)
    }

    @Test
    fun test_get_location_from_address_realty() {
        Mockito.`when`(geoCoder.getFromLocationName(any(), any())).thenReturn(listOf(address))

        Mockito.`when`(address.adminArea).thenReturn("Nord")
        Mockito.`when`(address.countryName).thenReturn("France")
        Mockito.`when`(address.locality).thenReturn("59")
        Mockito.`when`(address.subAdminArea).thenReturn("Nord-pas-de-calais")
        Mockito.`when`(address.longitude).thenReturn(0.0)
        Mockito.`when`(address.latitude).thenReturn(0.0)

        var test = utils.getLocationFromAddress(geoCoder, realtyModel.toRealty(emptyList()))

        assert("Nord" == test.region)
        assert("France" == test.country)
        assert("59" == test.city)
        assert("Nord-pas-de-calais" == test.department)
        assert(0.0 == test.longitude)
        assert(0.0 == test.latitude)
    }

    @Test
    fun test_date_from_string() {
        assert(Utils.getDateFromString("10/01/2022") == SimpleDateFormat("dd/MM/yyyy").parse("10/01/2022").time)
    }
}