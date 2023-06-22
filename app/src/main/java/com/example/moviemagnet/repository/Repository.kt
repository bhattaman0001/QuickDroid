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
    private val historyDatabase: HistoryDatabase?,
    private val savedFileRoomDatabase: SavedFileRoomDatabase?
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

    fun historyGetAllHistory() = historyDatabase?.getHistoryDao()?.getAllHistory()

    fun historyInsertOrUpdate(history: HistoryModel) =
        historyDatabase?.getHistoryDao()?.insertOrUpdate(history)

    fun responseInsertOrUpdate(file: ResponseModel) =
        savedFileRoomDatabase?.getFileDaa()?.insertOrUpdate(file)

    fun responseGetAllFile() = savedFileRoomDatabase?.getFileDaa()?.getAllFile()

    fun responseDeleteFile(file: ResponseModel) =
        savedFileRoomDatabase?.getFileDaa()?.deleteFile(file)

    fun historyDelete(history: HistoryModel) =
        historyDatabase?.getHistoryDao()?.deleteHistory(history)


    /*suspend fun responseInsertFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().insertFile(file)
    suspend fun responseUpdateFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().updateFile(file)*/
}