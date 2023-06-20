package com.example.moviemagnet.ui.activity

import android.annotation.*
import android.content.*
import android.os.*
import android.util.*
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.adapter.*
import com.example.moviemagnet.api.*
import com.example.moviemagnet.database.*
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.repository.*
import com.example.moviemagnet.util.*
import com.google.android.material.bottomsheet.*
import com.google.android.material.snackbar.*
import kotlinx.coroutines.*

class FileListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileListBinding
    var type_of_single_file_selected: String = ""
    var query_name: String = ""
    private lateinit var recyclerViewFileList: RecyclerView
    private lateinit var adapter: FileResponseAdapter
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        recyclerViewFileList = binding.fileListRv
        if (intent.extras != null) {
            query_name = intent.getStringExtra("query_name").toString()
            type_of_single_file_selected = intent.getStringExtra("type_of_single_file_selected").toString()
            /*Log.d("is_this_ok", "query_name --> $query_name | type_of_query --> $type_of_single_file_selected")*/
        }

        val history = HistoryModel(query_name, type_of_single_file_selected)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitHelper.response_api_interface.getData(
                    header1 = BuildConfig.header1,
                    header2 = BuildConfig.header2,
                    parameter1 = query_name,
                    parameter2 = type_of_single_file_selected
                )

                val responseTime = (response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis).toDouble() / 1000.0
                binding.responseTime.text = "Your request took $responseTime seconds to find and display! Thanks"
                binding.responseTime.isAllCaps = true

                /*Log.d("is_this_ok", "response is --> $response | response success --> ${response.isSuccessful}")*/
                Snackbar.make(view, "Response is ${response.body()?.status}", Snackbar.LENGTH_SHORT).show()
                if (response.isSuccessful) {
                    val data = response.body()?.files_found
                    binding.numberOfResults.text = "Total results : " + data?.size.toString()
                    binding.numberOfResults.isAllCaps = true
                    recyclerViewFileList.layoutManager = LinearLayoutManager(this@FileListActivity)

                    repository = Repository(SavedFileRoomDatabase(this@FileListActivity))
                    adapter = FileResponseAdapter(data?.distinct(), this@FileListActivity, repository)

                    // if the response is success then insert it into history db
                    repository = Repository(HistoryDatabase(this@FileListActivity))
                    repository.historyInsertOrUpdate(history)

                    recyclerViewFileList.adapter = adapter
                    /*Log.d("is_this_ok", "data size --> ${data?.size}")
                    Log.d("is_this_ok", "data  --> $data")*/
                } else {
                    /*Log.d("is_this_ok", "response is failed")*/
                    Snackbar.make(view, "The response is failed", Snackbar.LENGTH_SHORT).show()
                }
                binding.progressBar.visibility = INVISIBLE
                binding.responseTime.visibility = VISIBLE
                binding.numberOfResults.visibility = VISIBLE
                binding.divider.visibility = VISIBLE
            } catch (e: Exception) {
                /*Log.d("is_this_ok", "exception is --> ${e.message}\n${e.localizedMessage}")*/
                Snackbar.make(view, "Open your Internet", Snackbar.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("ResourceType")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val dialog = BottomSheetDialog(this, R.style.BottomDialog)
        val inflate: View = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, null)
        dialog.setContentView(inflate)
        val layoutParams: ViewGroup.LayoutParams = inflate.layoutParams
        layoutParams.width = this.resources.displayMetrics.widthPixels
        inflate.layoutParams = layoutParams
        dialog.window!!.setGravity(80)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setWindowAnimations(2131886298)
        dialog.show()

        inflate.findViewById<TextView>(R.id.go_to_home)?.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finishAffinity()
        }

        inflate.findViewById<TextView>(R.id.see_saved_files)?.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, SavedFilesActivity::class.java)
            startActivity(intent)
        }

        inflate.findViewById<TextView>(R.id.cancel_bottom_sheet)?.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)
        val item = menu?.findItem(R.id.go_to_history)
        item?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finishAffinity()
                return true
            }

            R.id.nav_share_app -> {
                Util.showComingSoonToast(this@FileListActivity)
                return true
            }

            R.id.nav_search_web -> {
                Util.showComingSoonToast(this@FileListActivity)
                return true
            }

            R.id.go_to_saved_file -> {
                val intent = Intent(this, SavedFilesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}