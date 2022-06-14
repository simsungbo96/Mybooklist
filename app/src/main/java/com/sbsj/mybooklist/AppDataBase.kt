package com.sbsj.mybooklist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sbsj.mybooklist.dao.HistoryDao
import com.sbsj.mybooklist.dao.ReviewDao
import com.sbsj.mybooklist.model.History
import com.sbsj.mybooklist.model.Review

@Database(entities = [History::class,Review::class],  version = 1)
abstract class AppDataBase :RoomDatabase(){
    abstract fun historyDao() : HistoryDao

    abstract fun reviewDao() : ReviewDao

}

fun getAppDatabase(context:Context) : AppDataBase{

    val migration_1_2 = object : Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `REVIEW` (`id` INTEGER, `review` TEXT," + "PRIMARY KEY(`id`))")
        }

    }
    return Room.databaseBuilder(
        context,
        AppDataBase::class.java,
        "BookSearchDB"
    ).build()

}