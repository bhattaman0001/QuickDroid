package com.example.moviemagnet.ui.activity

import android.*
import android.content.pm.*
import android.os.*
import android.view.*
import android.view.View.INVISIBLE
import androidx.appcompat.app.*
import androidx.core.content.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.Constants.Constants.header1
import com.example.moviemagnet.Constants.Constants.header2
import com.example.moviemagnet.adapter.*
import com.example.moviemagnet.api.*
import com.example.moviemagnet.databinding.*
import com.google.android.material.snackbar.*
import kotlinx.coroutines.*

class FileListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileListBinding
    var type_of_single_file_selected: String = ""
    var query_name: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileResponseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        recyclerView = binding.fileListRv
        if (intent.extras != null) {
            query_name = intent.getStringExtra("query_name").toString()
            type_of_single_file_selected = intent.getStringExtra("type_of_single_file_selected").toString()
            /*Log.d("is_this_ok", "query_name --> $query_name | type_of_query --> $type_of_single_file_selected")*/
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitHelper.response_api_interface.getData(
                        header1 = header1,
                        header2 = header2,
                        parameter1 = query_name,
                        parameter2 = type_of_single_file_selected
                    )
                    /*Log.d("is_this_ok", "response is --> $response | response success --> ${response.isSuccessful}")*/
                    Snackbar.make(view, "Response is ${response.body()?.status}", Snackbar.LENGTH_SHORT).show()
                    if (response.isSuccessful) {
                        val data = response.body()?.files_found
                        recyclerView.layoutManager = LinearLayoutManager(this@FileListActivity)
                        adapter = FileResponseAdapter(data, this@FileListActivity)
                        recyclerView.adapter = adapter
                        /*Log.d("is_this_ok", "data size --> ${data?.size}")
                        Log.d("is_this_ok", "data  --> $data")*/
                    } else {
                        /*Log.d("is_this_ok", "response is failed")*/
                        Snackbar.make(view, "The response is failed", Snackbar.LENGTH_SHORT).show()
                    }
                    binding.progressBar.visibility = INVISIBLE
                } catch (e: Exception) {
                    /*Log.d("is_this_ok", "exception is --> ${e.message}\n${e.localizedMessage}")*/
                    Snackbar.make(view, "The exception is --> ${e.message}", Snackbar.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        } else Snackbar.make(view, "Open your Internet", Snackbar.LENGTH_SHORT).show()
    }
}