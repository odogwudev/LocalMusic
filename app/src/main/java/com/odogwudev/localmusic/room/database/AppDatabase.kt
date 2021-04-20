package com.odogwudev.localmusic.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.odogwudev.localmusic.room.dao.SongDao
import com.odogwudev.localmusic.room.table.Song
import com.odogwudev.localmusic.utils.Constants

@Database(entities = [Song::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        private var mAppDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (mAppDatabase == null) {
                mAppDatabase = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java,
                    Constants.APP_NAME_DATABASE
                ).fallbackToDestructiveMigration().build()
            }
            return mAppDatabase!!
        }
    }
}