package com.openclassrooms.realestatemanager.database

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.dao.RealtyDao
import com.openclassrooms.realestatemanager.model.RealtyModel
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.app.RealEstateManagerApplication
import io.reactivex.rxjava3.core.Observable
import java.util.*


/**
 * Created by Julien Jennequin on 10/12/2021 13:28
 * Project : RealEstateManager
 **/

@Database(entities = [RealtyModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun realtyDao(): RealtyDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "realestate-database"
                    ).addCallback(CALLBACK).build()
                }
                return INSTANCE as AppDatabase
            }
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val realtyOne = ContentValues()
                realtyOne.put("id", 1)
                realtyOne.put("kind", "house")
                realtyOne.put("price", "1000")
                realtyOne.put("area", 150)
                realtyOne.put("room_number", 5)
                realtyOne.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyOne.put("address", "16 rue de lilas")
                realtyOne.put("point_of_interest", "Ecole,Cinema,Theatre")
                realtyOne.put("available", true)
                realtyOne.put("in_market_date", Date().time)
                realtyOne.put("out_market_date", Date().time)
                realtyOne.put("estate_agent", "Inspecteur Gadget")
                realtyOne.put("pictures","https://fr.depositphotos.com/7576104/stock-photo-luxurious-villa.html")


                val realtyTwo = ContentValues()
                realtyTwo.put("id", 2)
                realtyTwo.put("kind", "house")
                realtyTwo.put("price", "20000")
                realtyTwo.put("area", 1500)
                realtyTwo.put("room_number", 5)
                realtyTwo.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyTwo.put("address", "16 rue de lilas")
                realtyTwo.put("point_of_interest", "Ecole,Cinema,Theatre")
                realtyTwo.put("available", true)
                realtyTwo.put("in_market_date", Date().time)
                realtyTwo.put("out_market_date", Date().time)
                realtyTwo.put("estate_agent", "Inspecteur Gadget")
                realtyTwo.put("pictures","https://fr.depositphotos.com/7576104/stock-photo-luxurious-villa.html")


                val realtyThree = ContentValues()
                realtyThree.put("id", 3)
                realtyThree.put("kind", "Penthouse")
                realtyThree.put("price", "300000")
                realtyThree.put("area", 550)
                realtyThree.put("room_number", 5)
                realtyThree.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyThree.put("address", "16 rue de lilas")
                realtyThree.put("point_of_interest", "Ecole,Cinema,Theatre")
                realtyThree.put("available", true)
                realtyThree.put("in_market_date", Date().time)
                realtyThree.put("out_market_date", Date().time)
                realtyThree.put("estate_agent", "Inspecteur Gadget")
                realtyThree.put("pictures","https://cf.bstatic.com/xdata/images/hotel/max1024x768/304738515.jpg?k=e9920790ed2889f167da1be8e39233adb331cba7014f508cd34c67a9ef4fa09b&o=&hp=1")


                val realtyFour = ContentValues()
                realtyFour.put("id", 4)
                realtyFour.put("kind", "Villa")
                realtyFour.put("price", "4000000")
                realtyFour.put("area", 1250)
                realtyFour.put("room_number", 5)
                realtyFour.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyFour.put("address", "16 rue de lilas")
                realtyFour.put("point_of_interest", "Ecole,Cinema,Theatre")
                realtyFour.put("available", true)
                realtyFour.put("in_market_date", Date().time)
                realtyFour.put("out_market_date", Date().time)
                realtyFour.put("estate_agent", "Inspecteur Gadget")
                realtyFour.put("pictures","https://fr.depositphotos.com/7576104/stock-photo-luxurious-villa.html")


                val realtyFive= ContentValues()
                realtyFive.put("id", 5)
                realtyFive.put("kind", "Villa")
                realtyFive.put("price", "50000000")
                realtyFive.put("area", 1505)
                realtyFive.put("room_number", 5)
                realtyFive.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyFive.put("address", "16 rue de lilas")
                realtyFive.put("point_of_interest", "Ecole,Cinema,Theatre")
                realtyFive.put("available", true)
                realtyFive.put("in_market_date", Date().time)
                realtyFive.put("out_market_date", Date().time)
                realtyFive.put("estate_agent", "Inspecteur Gadget")
                realtyFive.put("pictures","https://www.nevadomarbella.com/cms/wp-content/uploads/2014/10/Living-in-Marbella-%E2%80%93-Penthouse-or-Villa-Nevado-Realty-Real-Estate-in-Marbella-700x312.jpg")

                val realtySix= ContentValues()
                realtySix.put("id", 6)
                realtySix.put("kind", "Villa")
                realtySix.put("price", "600000000")
                realtySix.put("area", 150)
                realtySix.put("room_number", 4)
                realtySix.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtySix.put("address", "16 rue de Jaspine")
                realtySix.put("point_of_interest", "Ecole,Cinema,Theatre")
                realtySix.put("available", true)
                realtySix.put("in_market_date", Date().time)
                realtySix.put("out_market_date", Date().time)
                realtySix.put("estate_agent", "Inspecteur Gadget")
                realtySix.put("pictures","https://www.nevadomarbella.com/cms/wp-content/uploads/2014/10/Living-in-Marbella-%E2%80%93-Penthouse-or-Villa-Nevado-Realty-Real-Estate-in-Marbella-5-700x448.jpg")

                val realtySeven= ContentValues()
                realtySeven.put("id", 7)
                realtySeven.put("kind", "Villa")
                realtySeven.put("price", "7000000000")
                realtySeven.put("area", 150)
                realtySeven.put("room_number", 7)
                realtySeven.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtySeven.put("address", "16 rue de lilas")
                realtySeven.put("point_of_interest", "Ecole,Cinema,Theatre")
                realtySeven.put("available", true)
                realtySeven.put("in_market_date", Date().time)
                realtySeven.put("out_market_date", Date().time)
                realtySeven.put("estate_agent", "Inspecteur Gadget")
                realtySeven.put("pictures","https://cdn.villa-finder.com/cache/1024/villas/sky-villa-penthouse/sky-villa-penthouse-17-70a-devasom-sky-villa-two-bedroom-pool-penthouse-5eaf915c2807e.JPG")


                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyOne)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyTwo)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyThree)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyFour)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyFive)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtySix)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtySeven)
            }
        }
    }

}