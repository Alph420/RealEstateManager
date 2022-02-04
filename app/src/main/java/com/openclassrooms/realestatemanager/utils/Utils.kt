package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.*
import android.os.Build
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import android.provider.MediaStore.Images
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel
import android.net.ConnectivityManager


/**
 * Created by Julien Jennequin on 02/12/2021 15:35
 * Project : RealEstateManager
 **/
object Utils {

    //region Don't touch
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * 0.812).roundToInt()
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    fun getTodayDate(time: Long): String {
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(Date(time))
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(connectivityManager: ConnectivityManager): Boolean {
        //TODO IMPROVE THIS GET BY RETURN WHICH NETWORK IS USED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
    //endregion

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * @param euros
     * @return
     */
    fun convertEurosToDollars(euros: Int): Int {
        return (euros * 1.131).toInt()
    }

    fun formatPrice(price: Long): String {
        val priceText = StringBuilder()
        when {
            (price.toString().length) <= 3 -> {
                return price.toString()
            }
            (price.toString().length) <= 6 && (price.toString().length) >= 4 -> {
                priceText
                    .append(price.toString().substring(0, price.toString().length - 3))
                    .append(',')
                    .append(
                        price.toString()
                            .substring(price.toString().length - 3, price.toString().length)
                    )
            }
            (price.toString().length) > 6 && (price.toString().length) <= 7 -> {
                priceText
                    .append(price.toString().substring(0, 1))
                    .append(',')
                    .append(price.toString().substring(1, price.toString().length - 3))
                    .append(',')
                    .append(
                        price.toString()
                            .substring(price.toString().length - 3, price.toString().length)
                    )
            }
            (price.toString().length) == 8 -> {
                priceText
                    .append(price.toString().substring(0, 2))
                    .append(',')
                    .append(price.toString().substring(2, price.toString().length - 3))
                    .append(',')
                    .append(
                        price.toString()
                            .substring(price.toString().length - 3, price.toString().length)
                    )
            }
            (price.toString().length) == 9 -> {
                priceText
                    .append(price.toString().substring(0, 3))
                    .append(',')
                    .append(price.toString().substring(3, price.toString().length - 3))
                    .append(',')
                    .append(
                        price.toString()
                            .substring(price.toString().length - 3, price.toString().length)
                    )
            }
            (price.toString().length) == 10 -> {
                priceText
                    .append(price.toString().substring(0, 1))
                    .append(',')
                    .append(price.toString().substring(1, price.toString().length - 6))
                    .append(',')
                    .append(
                        price.toString()
                            .substring(price.toString().length - 6, price.toString().length - 3)
                    )
                    .append(',')
                    .append(
                        price.toString()
                            .substring(price.toString().length - 3, price.toString().length)
                    )
            }
        }
        return priceText.toString()
    }

    fun getLocationFromAddress(geoCoder: Geocoder, realty: RealtyModel): RealtyModel {
        if (geoCoder.getFromLocationName(realty.address, 5).size > 0) {
            val address: Address = geoCoder.getFromLocationName(realty.address, 5)[0]
            realty.region = address.adminArea
            realty.country = address.countryName
            realty.city = address.locality
            realty.department = address.subAdminArea
            realty.longitude = address.longitude
            realty.latitude = address.latitude
        }

        return realty
    }

    fun getLocationFromAddress(geoCoder: Geocoder, realty: Realty): Realty {
        if (geoCoder.getFromLocationName(realty.address, 5).size > 0) {
            val address: Address = geoCoder.getFromLocationName(realty.address, 5)[0]
            realty.region = address.adminArea
            realty.country = address.countryName
            realty.city = address.locality
            realty.department = address.subAdminArea
            realty.longitude = address.longitude
            realty.latitude = address.latitude
        }

        return realty
    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getDateFromString(date: String): Long {
        return if (date.isEmpty()) 0L
        else SimpleDateFormat("dd/MM/yyyy").parse(date).time
    }
}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}
