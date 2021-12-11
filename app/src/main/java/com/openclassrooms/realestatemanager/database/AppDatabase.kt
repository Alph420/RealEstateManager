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
                realtyOne.put("id",1)
                realtyOne.put("kind","house")
                realtyOne.put("price","10000000")




               db.insert("RealtyModel", OnConflictStrategy.IGNORE,realtyOne)
            }
        }
    }

}