package com.example.moviemagnet

import android.app.Application
import com.example.moviemagnet.data.db.database.HistoryDatabase
import com.example.moviemagnet.data.db.database.SavedFileRoomDatabase
import com.example.moviemagnet.data.repository.Repository
import kotlin.properties.Delegates

class FileApplication : Application() {
    lateinit var hRepo: Repository
    lateinit var sRepo: Repository
    private var isVisible: Boolean = false

    override fun onCreate() {
        super.onCreate()
        initialize()
//        setButtonVisibility()
    }

    private fun initialize() {
        hRepo = Repository(HistoryDatabase(this))
        sRepo = Repository(SavedFileRoomDatabase(this))
    }

}