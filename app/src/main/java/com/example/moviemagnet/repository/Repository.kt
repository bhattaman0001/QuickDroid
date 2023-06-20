package com.example.moviemagnet.repository

import androidx.room.*
import com.example.moviemagnet.database.*
import com.example.moviemagnet.model.*

class Repository(private val db: RoomDatabase) {

    /*suspend fun historyInsert(history: HistoryModel) = (database as HistoryDatabase).getHistoryDao().insert(history)
    suspend fun historyUpdate(history: HistoryModel) = (database as HistoryDatabase).getHistoryDao().update(history)*/


    fun historyGetAllHistory() = (db as HistoryDatabase).getHistoryDao().getAllHistory()

    suspend fun historyInsertOrUpdate(history: HistoryModel) = (db as HistoryDatabase).getHistoryDao().insertOrUpdate(history)

    suspend fun responseInsertOrUpdate(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().insertOrUpdate(file)

    fun responseGetAllFile() = (db as SavedFileRoomDatabase).getFileDaa().getAllFile()

    suspend fun responseDeleteFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().deleteFile(file)





    /*suspend fun responseInsertFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().insertFile(file)
    suspend fun responseUpdateFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().updateFile(file)*/
}