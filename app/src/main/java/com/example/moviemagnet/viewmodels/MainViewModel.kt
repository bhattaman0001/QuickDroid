package com.example.moviemagnet.viewmodels

import android.app.*
import androidx.lifecycle.*
import com.example.moviemagnet.model.FileFoundModel
import com.example.moviemagnet.model.HistoryModel
import com.example.moviemagnet.model.ResponseModel
import com.example.moviemagnet.repository.*
import com.example.moviemagnet.util.Resource

class MainViewModel(
    val app: Application, private val repository: Repository
) : AndroidViewModel(app) {
    private val response: MutableLiveData<Resource<FileFoundModel>> = MutableLiveData()
    private val history: MutableLiveData<Resource<HistoryModel>> = MutableLiveData()
    private val savedFile: MutableLiveData<Resource<ResponseModel>> = MutableLiveData()

}