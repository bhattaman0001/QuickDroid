package com.example.moviemagnet.util

import android.content.*
import android.widget.*
import com.example.moviemagnet.db.*
import com.example.moviemagnet.model.*
import kotlinx.coroutines.*

object Util {
    const val BASE_URL = "https://filepursuit.p.rapidapi.com/"
    const val message = "Thanks for using our application, below is the link of the file that you are trying to send to this beautiful person\n\n"

    fun saveFile(file: ResponseModel, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase.invoke(context).getFileDaa().insertFile(file)
    }

    fun insertOrUpdate(file: ResponseModel, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase(context).getFileDaa().insertOrUpdate(file)
    }

    fun insertOrUpdateHistory(history: HistoryModel, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        HistoryDatabase(context).getHistoryDao().insertOrUpdate(history)
    }

    fun updateFile(file: ResponseModel, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase.invoke(context).getFileDaa().updateFile(file)
    }

    fun deleteAllFile(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase(context).clearAllTables()
    }

    fun deleteFile(context: Context, file: ResponseModel) = CoroutineScope(Dispatchers.IO).launch {
        SavedFileRoomDatabase(context).getFileDaa().deleteFile(file)
    }

    fun showInsertToast(context: Context) {
        Toast.makeText(context, "Item Saved", Toast.LENGTH_SHORT).show()
    }

    fun showDeleteToast(context: Context) {
        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
    }

    fun alreadyExistToast(context: Context) {
        Toast.makeText(context, "Item Already Saved", Toast.LENGTH_SHORT).show()
    }

    fun showComingSoonToast(context: Context) {
        Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
    }

}