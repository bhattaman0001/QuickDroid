package com.example.moviemagnet

import android.app.Application
import androidx.room.RoomDatabase
import com.example.moviemagnet.data.repository.Repository

class FileApplication(private val db: RoomDatabase) : Application() {
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        repository = Repository(db)
    }
}