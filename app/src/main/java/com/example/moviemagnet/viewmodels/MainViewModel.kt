package com.example.moviemagnet.viewmodels

import android.app.*
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.moviemagnet.FileApplication
import com.example.moviemagnet.model.FileFoundModel
import com.example.moviemagnet.model.HistoryModel
import com.example.moviemagnet.model.ResponseModel
import com.example.moviemagnet.repository.*
import com.example.moviemagnet.util.Resource
import retrofit2.Response
import java.io.IOException

class MainViewModel(
    val app: Application, private val repository: Repository
) : AndroidViewModel(app) {
    private val responseLiveData: MutableLiveData<Resource<FileFoundModel>> = MutableLiveData()
    private val history: MutableLiveData<Resource<HistoryModel>> = MutableLiveData()
    private val savedFile: MutableLiveData<Resource<ResponseModel>> = MutableLiveData()

    /*private suspend fun getFileResponse(searchQuery: String, searchType: String) {
        responseLiveData.postValue(Resource.Loading())
        try {
            if (isOnline()) {
                val response = repository.getFileFound(searchQuery, searchType)
                responseLiveData.postValue(handleResponseFile(response))
            } else {
                responseLiveData.postValue(Resource.Error("No Internet Connection Available"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> responseLiveData.postValue(Resource.Error("Network Failure"))
                else -> responseLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleResponseFile(response: Response<FileFoundModel>): Resource<FileFoundModel> {
        if (response.isSuccessful) {

        }
        return Resource.Error(response.message())
    }*/

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

    val responseFile: LiveData<Resource<FileFoundModel>> get() = responseLiveData

}