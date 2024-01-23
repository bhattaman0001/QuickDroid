package com.example.moviemagnet.data.repository

import androidx.room.*
import com.example.moviemagnet.BuildConfig
import com.example.moviemagnet.api.RetrofitHelper
import com.example.moviemagnet.data.db.database.HistoryDatabase
import com.example.moviemagnet.data.db.database.SavedFileRoomDatabase
import com.example.moviemagnet.model.HistoryModel
import com.example.moviemagnet.model.ResponseModel

class Repository(
    val db: RoomDatabase
) {
    suspend fun getFileFound(searchQuery: String, searchType: String) =
        RetrofitHelper.apiInterface.getData(header1 = BuildConfig.header1, header2 = BuildConfig.header2, parameter1 = searchQuery, parameter2 = searchType)

    fun historyGetAllHistory() = (db as HistoryDatabase).getHistoryDao().getAllHistory()

    fun historyInsertOrUpdate(history: HistoryModel) = (db as HistoryDatabase).getHistoryDao().insertOrUpdate(history)

    fun historyDelete(history: HistoryModel) = (db as HistoryDatabase).getHistoryDao().deleteHistory(history)

    fun insertOrUpdateSavedFiles(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().insertOrUpdate(file)

    fun getSavedFiles() = (db as SavedFileRoomDatabase).getFileDaa().getAllFile()

    fun deleteSavedFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().deleteFile(file)

    /*suspend fun responseInsertFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().insertFile(file)
    suspend fun responseUpdateFile(file: ResponseModel) = (db as SavedFileRoomDatabase).getFileDaa().updateFile(file)*/
}