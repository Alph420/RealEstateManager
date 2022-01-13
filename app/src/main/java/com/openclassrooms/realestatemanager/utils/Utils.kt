package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.net.wifi.WifiManager
import androidx.room.TypeConverter
import com.google.firebase.firestore.GeoPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import android.provider.MediaStore.Images
import android.provider.MediaStore
import com.openclassrooms.realestatemanager.model.Realty
import com.openclassrooms.realestatemanager.model.RealtyModel


/**
 * Created by Julien Jennequin on 02/12/2021 15:35
 * Project : RealEstateManager
 **/
internal object Utils {

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
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
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

    fun getLocationFromAddress(context: Context, realty: RealtyModel): RealtyModel {
        if (Geocoder(context).getFromLocationName(realty.address, 5).size > 0) {
            val location: Address = Geocoder(context).getFromLocationName(realty.address, 5)[0]
            realty.region = location.adminArea
            realty.country = location.countryName
            realty.city = location.locality
            realty.department = location.subAdminArea
            realty.longitude = location.longitude
            realty.latitude = location.latitude
        }

        return realty
    }

    fun getLocationFromAddress(context: Context, realty: Realty): Realty {
        if (Geocoder(context).getFromLocationName(realty.address, 5).size > 0) {
            val location: Address = Geocoder(context).getFromLocationName(realty.address, 5)[0]
            realty.region = location.adminArea
            realty.country = location.countryName
            realty.city = location.locality
            realty.department = location.subAdminArea
            realty.longitude = location.longitude
            realty.latitude = location.latitude
        }

        return realty
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
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
