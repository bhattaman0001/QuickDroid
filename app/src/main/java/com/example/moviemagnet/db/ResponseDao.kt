package com.example.moviemagnet.db

import androidx.lifecycle.*
import androidx.room.*
import com.example.moviemagnet.model.*
import kotlin.coroutines.*

@Dao
interface ResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: ResponseModel): Long

    @Query("SELECT * FROM FOUNDFILE")
    fun getAllFile(): LiveData<List<ResponseModel>>

    @Delete
    suspend fun deleteFile(file: ResponseModel)

    @Update
    suspend fun updateFile(file: ResponseModel): Int

    @Transaction
    suspend fun insertOrUpdate(file: ResponseModel): Any? {
        val id = insertFile(file)
        return if (id == -1L) {
            updateFile(file)
            file.file_link
        } else {
            id
        }
    }
}