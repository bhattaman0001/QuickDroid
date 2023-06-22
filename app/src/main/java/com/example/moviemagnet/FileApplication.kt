package com.example.moviemagnet

import android.app.Application
import androidx.room.RoomDatabase
import com.example.moviemagnet.database.HistoryDatabase
import com.example.moviemagnet.database.SavedFileRoomDatabase
import com.example.moviemagnet.repository.Repository

class FileApplication : Application() {
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        repository = Repository(HistoryDatabase(this), SavedFileRoomDatabase(this))
    }
}