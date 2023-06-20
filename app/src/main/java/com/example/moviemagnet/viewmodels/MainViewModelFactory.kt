package com.example.moviemagnet.viewmodels

import android.app.*
import androidx.lifecycle.*
import com.example.moviemagnet.repository.*

class MainViewModelFactory(
    val app: Application, private val repository: Repository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app, repository) as T
    }
}