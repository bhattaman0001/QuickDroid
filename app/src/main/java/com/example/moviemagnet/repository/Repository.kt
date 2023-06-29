package com.example.moviemagnet.repository

import androidx.room.*
import com.example.moviemagnet.BuildConfig
import com.example.moviemagnet.api.RetrofitHelper
import com.example.moviemagnet.database.*
import com.example.moviemagnet.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(
    private val db: RoomDatabase
) {

    /*suspend fun historyInsert(history: HistoryModel) = (database as HistoryDatabase).getHistoryDao().insert(history)
    suspend fun historyUpdate(history: HistoryModel) = (database as HistoryDatabase).getHistoryDao().update(history)*/

    suspend fun getFileFound(searchQuery: String, searchType: String) {
        RetrofitHelper.response_api_interface.getData(
            header1 = BuildConfig.header1,
            header2 = BuildConfig.header2,
            parameter1 = searchQuery,
            parameter2 = searchType
        )
    }

    fun historyGetAllHistory() = (db as HistoryDatabase).getHistoryDao().getAllHistory()

    fun historyInsertOrUpdate(history: HistoryModel) =
        (db as HistoryDatabase).getHistoryDao().insertOrUpdate(history)

    fun historyDelete(history: HistoryModel) =
        (db as HistoryDatabase).getHistoryDao().deleteHistory(history)

    fun responseInsertOrUpdate(file: ResponseModel) =
        (db as SavedFileRoomDatabase).getFileDaa().insertOrUpdate(file)

    fun responseGetAllFile() =
        (db as SavedFileRoomDatabase).getFileDaa().getAllFile()

    fun responseDeleteFile(file: ResponseModel) =
        (db as SavedFileRoomDatabase).getFileDaa().deleteFile(file)


    /*suspend fun responseInsertFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().insertFile(file)
    suspend fun responseUpdateFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().updateFile(file)*/
}