package com.example.moviemagnet.dao

import androidx.lifecycle.*
import androidx.room.*
import com.example.moviemagnet.model.*

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryModel): Long

    @Update
    suspend fun update(history: HistoryModel): Int

    @Query("SELECT * FROM History")
    fun getAllHistory(): LiveData<List<HistoryModel>>

    @Transaction
    suspend fun insertOrUpdate(history: HistoryModel): Any? {
        val id = insert(history)
        return if (id == -1L) {
            update(history)
            history.queryType
        } else {
            id
        }
    }
}