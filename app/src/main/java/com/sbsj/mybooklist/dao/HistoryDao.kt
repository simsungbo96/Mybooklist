package com.sbsj.mybooklist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sbsj.mybooklist.model.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM History")
    fun getAll() :List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM HISTORY WHERE keyword == :keyword")
    fun delete(keyword :String)
}