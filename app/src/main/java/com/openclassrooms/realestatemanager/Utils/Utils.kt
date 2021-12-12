package com.openclassrooms.realestatemanager.Utils

import android.content.Context
import android.net.wifi.WifiManager
import com.openclassrooms.realestatemanager.R
import java.lang.StringBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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
    fun getTodayDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        return dateFormat.format(Date())
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }
    //endregion


    fun formatPrice(price: Long): String {
        val priceText = StringBuilder()
        when {
            (price.toString().length) <= 6 -> {
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

}