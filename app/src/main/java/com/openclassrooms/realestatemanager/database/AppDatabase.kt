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

@Database(entities = [RealtyModel::class,PicturesModel::class], version = 1, exportSchema = false)
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
                realtyOne.put("longitude",0.0)
                realtyOne.put("latitude",0.0)
                realtyOne.put("point_of_interest", "Ecole,Cinema,Theatre")
                realtyOne.put("available", true)
                realtyOne.put("in_market_date", Date().time)
                realtyOne.put("out_market_date", Date().time)
                realtyOne.put("estate_agent", "Inspecteur Gadget")


                db.insert("RealtyModel", OnConflictStrategy.IGNORE, realtyOne)
            }
        }
    }

}