package com.example.moviemagnet.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.moviemagnet.FileApplication
import com.example.moviemagnet.model.HistoryModel
import com.example.moviemagnet.model.ResponseModel
import com.example.moviemagnet.data.repository.Repository
import com.example.moviemagnet.model.FileFoundModel
import com.example.moviemagnet.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

@Suppress("UNCHECKED_CAST")
class MainViewModel(
    application: Application, private val repository: Repository
) : AndroidViewModel(application) {

    private val _responseLiveData: MutableLiveData<Resource<FileFoundModel>> = MutableLiveData()

    var fileResponseResult: FileFoundModel? = null
    var responseTime: Double? = null
    var numberOfResult: String? = null

    private var getHistoryAllData: MutableLiveData<Resource<LiveData<List<HistoryModel>>>> = MutableLiveData()

    private var getSavedAllFiles: MutableLiveData<Resource<LiveData<List<ResponseModel>>>> = MutableLiveData()

    fun getAllHistoryFile() = viewModelScope.launch {
        safeGetAllHistoryFile()
    }

    fun getAllSavedFile() = viewModelScope.launch {
        safeGetAllSavedFile()
    }

    private fun safeGetAllSavedFile() {
        getSavedAllFiles.postValue(Resource.Loading())
        val savedFiles = repository.getSavedFiles()
        getSavedAllFiles.postValue(handleSavedFiles(savedFiles))
    }

    private fun handleSavedFiles(savedFiles: LiveData<List<ResponseModel>>): Resource<LiveData<List<ResponseModel>>> {
        return Resource.Success(savedFiles, 0.0, "")
    }

    private fun safeGetAllHistoryFile() {
        getHistoryAllData.postValue(Resource.Loading())
        val history = repository.historyGetAllHistory()
        getHistoryAllData.postValue(handleHistory(history))
    }

    private fun handleHistory(history: LiveData<List<HistoryModel>>): Resource<LiveData<List<HistoryModel>>> {
        return Resource.Success(history, 0.0, "")
    }

    fun getRequestResponse(queryName: String, typeofFileSelected: String) = viewModelScope.launch {
        safeGetRequestResponse(queryName, typeofFileSelected)
    }

    private suspend fun safeGetRequestResponse(queryName: String, typeofFileSelected: String) {
        _responseLiveData.postValue(Resource.Loading())
        try {
            if (isOnline()) {
                val response = repository.getFileFound(queryName, typeofFileSelected)
                _responseLiveData.postValue(handleResponse(response))
            } else {
                _responseLiveData.postValue(Resource.Error("No Internet Connection Available"))
            }
        } catch (e: Exception) {
            when (e) {
                is IOException -> _responseLiveData.postValue(Resource.Error("Network Failure"))
                else -> _responseLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleResponse(response: Response<FileFoundModel>): Resource<FileFoundModel> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                fileResponseResult = responseResult
                responseTime = (response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis).toDouble() / 1000.0
                val data = response.body()?.files_found
                numberOfResult = data?.size.toString()
                return Resource.Success(fileResponseResult ?: responseResult, responseTime!!, numberOfResult!!)
            }
        }
        return Resource.Error(response.message())
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getApplication<FileApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    companion object {
        class MainViewModelFactory(
            private val application: Application, private val repository: Repository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(application, repository) as T
            }
        }
    }

    val responseLiveData: LiveData<Resource<FileFoundModel>>
        get() = _responseLiveData

    val getAllHistoryFile: LiveData<Resource<LiveData<List<HistoryModel>>>>
        get() = getHistoryAllData

    val getAllSavedFile: LiveData<Resource<LiveData<List<ResponseModel>>>>
        get() = getSavedAllFiles

}