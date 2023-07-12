package com.example.moviemagnet.ui.viewmodels

import android.app.*
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.moviemagnet.FileApplication
import com.example.moviemagnet.data.repository.Repository
import com.example.moviemagnet.model.FileFoundModel
import com.example.moviemagnet.data.db.entity.HistoryModel
import com.example.moviemagnet.data.db.entity.ResponseModel
import com.example.moviemagnet.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel(
    val app: Application, private val repository: Repository
) : AndroidViewModel(app) {

    private val getFiles: MutableLiveData<Resource<FileFoundModel>> = MutableLiveData()
    var getFilesResponse: FileFoundModel? = null

    val gFiles: LiveData<Resource<FileFoundModel>>
        get() = getFiles

    fun getFiles(searchQuery: String, searchType: String) = viewModelScope.launch {
        getFilesCall(searchQuery, searchType)
    }



    private suspend fun getFilesCall(searchQuery: String, searchType: String) {
        getFiles.postValue(Resource.Loading())
        try {
            if (isOnline()) {
                val response = repository.getFileFound(searchQuery, searchType)
                getFiles.postValue(handleFileCall(response))
            } else {
                getFiles.postValue(Resource.Error("No Internet Connection Available"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getFiles.postValue(Resource.Error("Network Failure"))
                else -> getFiles.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleFileCall(response: Response<FileFoundModel>): Resource<FileFoundModel> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (getFilesResponse == null) {
                    getFilesResponse = it
                } else {
                    val oldFiles = getFilesResponse?.files_found
                    val newFiles = it.files_found
                    oldFiles?.addAll(newFiles)
                }
                return Resource.Success(getFilesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }


    private fun isOnline(): Boolean {
        val connectivityManager =
            getApplication<FileApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

}