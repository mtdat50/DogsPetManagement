package com.example.dogspetmanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Dog::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dogDao(): DogDao



    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null)
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "DogDB").build()
                }

            return INSTANCE!!
        }
    }
}