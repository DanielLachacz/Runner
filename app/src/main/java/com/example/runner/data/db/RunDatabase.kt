package com.example.runner.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.runner.data.model.Run

@Database(entities = [Run::class], version = 4)
abstract class RunDatabase: RoomDatabase() {

    abstract fun runDao(): RunDao

    companion object {
        @Volatile private var instance: RunDatabase? = null
        private val LOCK = Any()

        fun getDatabase(context: Context): RunDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    RunDatabase::class.java,
                    "run_database"
                ).fallbackToDestructiveMigration()
                    .build()
                instance = instance
                return instance
            }
        }
    }
}