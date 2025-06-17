package com.tpjm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tpjm.model.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sign_language_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}