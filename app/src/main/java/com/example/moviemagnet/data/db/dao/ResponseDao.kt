package com.example.moviemagnet.data.db.dao

import androidx.lifecycle.*
import androidx.room.*
import com.example.moviemagnet.model.ResponseModel

@Dao
interface ResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFile(file: ResponseModel): Long

    @Query("SELECT * FROM foundFile")
    fun getAllFile(): LiveData<List<ResponseModel>>

    @Delete
    fun deleteFile(file: ResponseModel)

    @Update
    fun updateFile(file: ResponseModel): Int

    @Transaction
    fun insertOrUpdate(file: ResponseModel): Any? {
        val id = insertFile(file)
        return if (id == -1L) {
            updateFile(file)
            file.id
        } else {
            id
        }
    }
}