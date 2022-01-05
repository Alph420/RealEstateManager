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
import com.openclassrooms.realestatemanager.dao.PictureDao
import com.openclassrooms.realestatemanager.model.PicturesModel
import io.reactivex.rxjava3.core.Observable
import java.util.*


/**
 * Created by Julien Jennequin on 10/12/2021 13:28
 * Project : RealEstateManager
 **/

@Database(entities = [RealtyModel::class, PicturesModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun realtyDao(): RealtyDao
    abstract fun pictureDao(): PictureDao


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

                //region realtyOne
                val realtyOne = ContentValues()
                realtyOne.put("id", 1)
                realtyOne.put("kind", "house")
                realtyOne.put("price", "1000")
                realtyOne.put("area", 150)
                realtyOne.put("room_number", 6)
                realtyOne.put("bathroom_number", 2)
                realtyOne.put("bedroom_number", 4)
                realtyOne.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyOne.put("address", "16 rue de lilas")
                realtyOne.put("region", "")
                realtyOne.put("country", "")
                realtyOne.put("city", "")
                realtyOne.put("department", "")
                realtyOne.put("longitude", 0.0)
                realtyOne.put("latitude", 0.0)
                realtyOne.put("point_of_interest", "School, Cinema, Library")
                realtyOne.put("available", true)
                realtyOne.put("in_market_date", Date().time)
                realtyOne.put("out_market_date", Date().time)
                realtyOne.put("estate_agent", "Inspecteur Gadget")

                //endregion

                //region realtyTwo
                val realtyTwo = ContentValues()
                realtyTwo.put("id", 2)
                realtyTwo.put("kind", "Penthouse")
                realtyTwo.put("price", "2000000")
                realtyTwo.put("area", 3000)
                realtyTwo.put("room_number", 9)
                realtyTwo.put("bathroom_number", 4)
                realtyTwo.put("bedroom_number", 5)
                realtyTwo.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyTwo.put("address", "16 rue Lille")
                realtyTwo.put("region", "")
                realtyTwo.put("country", "")
                realtyTwo.put("city", "")
                realtyTwo.put("department", "")
                realtyTwo.put("longitude", 50.633906)
                realtyTwo.put("latitude", 3.058742)
                realtyTwo.put("point_of_interest", "School, Cinema, Library, Bank")
                realtyTwo.put("available", true)
                realtyTwo.put("in_market_date", Date().time)
                realtyTwo.put("out_market_date", Date().time)
                realtyTwo.put("estate_agent", "Sonic")

                //endregion

                //region realtyThree
                val realtyThree = ContentValues()
                realtyThree.put("id", 3)
                realtyThree.put("kind", "Villa")
                realtyThree.put("price", "8500000")
                realtyThree.put("area", 150)
                realtyThree.put("room_number", 3)
                realtyThree.put("bathroom_number", 1)
                realtyThree.put("bedroom_number", 2)
                realtyThree.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyThree.put("address", "16 rue de paris")
                realtyThree.put("region", "")
                realtyThree.put("country", "")
                realtyThree.put("city", "")
                realtyThree.put("department", "")
                realtyThree.put("longitude", 48.854136)
                realtyThree.put("latitude", 2.254200)
                realtyThree.put("point_of_interest", "School, Cinema, Library, Bank")
                realtyThree.put("available", true)
                realtyThree.put("in_market_date", Date().time)
                realtyThree.put("out_market_date", Date().time)
                realtyThree.put("estate_agent", "Mike Tyson")

                //endregion

                //region realtyFour
                val realtyFour = ContentValues()
                realtyFour.put("id", 4)
                realtyFour.put("kind", "house")
                realtyFour.put("price", "1000")
                realtyFour.put("area", 150)
                realtyFour.put("room_number", 6)
                realtyFour.put("bathroom_number", 2)
                realtyFour.put("bedroom_number", 4)
                realtyFour.put(
                    "description",
                    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                )
                realtyFour.put("address", "16 rue biarritz")
                realtyFour.put("region", "")
                realtyFour.put("country", "")
                realtyFour.put("city", "")
                realtyFour.put("department", "")
                realtyFour.put("longitude", 43.552240)
                realtyFour.put("latitude", -1.446746)
                realtyFour.put("point_of_interest", "School, Cinema, Library")
                realtyFour.put("available", true)
                realtyFour.put("in_market_date", Date().time)
                realtyFour.put("out_market_date", Date().time)
                realtyFour.put("estate_agent", "Inspecteur Gadget")
                //endregion

                //region INSERT
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyOne)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyTwo)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyThree)
                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyFour)
                //endregion
            }
        }
    }

}