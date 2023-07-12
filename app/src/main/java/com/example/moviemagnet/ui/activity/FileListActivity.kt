package com.example.moviemagnet.ui.activity

import android.annotation.*
import android.content.*
import android.os.*
import android.util.*
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.*
import com.example.moviemagnet.BuildConfig
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.api.*
import com.example.moviemagnet.data.db.database.HistoryDatabase
import com.example.moviemagnet.data.db.database.SavedFileRoomDatabase
import com.example.moviemagnet.data.db.entity.HistoryModel
import com.example.moviemagnet.data.repository.Repository
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.ui.adapter.FileResponseAdapter
import com.example.moviemagnet.util.*
import com.google.android.material.bottomsheet.*
import com.google.android.material.snackbar.*
import kotlinx.coroutines.*

class FileListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileListBinding
    private var typeOfSingleFileSelected: String = ""
    private var queryName: String = ""
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
            queryName = intent.getStringExtra("query_name").toString()
            typeOfSingleFileSelected =
                intent.getStringExtra("type_of_single_file_selected").toString()
        }

        val history = HistoryModel(queryName, typeOfSingleFileSelected)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitHelper.responseApiInterface.getData(
                    header1 = BuildConfig.header1,
                    header2 = BuildConfig.header2,
                    parameter1 = queryName,
                    parameter2 = typeOfSingleFileSelected
                )
                val responseTime = (response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis).toDouble() / 1000.0
                binding.responseTime.text =
                    "Your request took $responseTime seconds to find and display! Thanks"
                binding.responseTime.isAllCaps = true

                Snackbar.make(view, "Response is ${response.body()?.status}", Snackbar.LENGTH_SHORT)
                    .show()
                if (response.isSuccessful) {
                    val data = response.body()?.files_found

                    runOnUiThread(Runnable {
                        run {
                            binding.shimmerFrameLayout.stopShimmer()
                            binding.responseTime.visibility = VISIBLE
                            binding.numberOfResults.visibility = VISIBLE
                            binding.shimmerFrameLayout.visibility = GONE
                            recyclerViewFileList.visibility = VISIBLE
                        }
                    })

                    binding.numberOfResults.text = "Total results : " + data?.size.toString()
                    binding.numberOfResults.isAllCaps = true
                    recyclerViewFileList.layoutManager = LinearLayoutManager(this@FileListActivity)

                    repository = Repository(SavedFileRoomDatabase(this@FileListActivity))
                    adapter =
                        FileResponseAdapter(data?.distinct(), this@FileListActivity, repository)

                    // if the response is success then insert it into history db
                    repository = Repository(HistoryDatabase(this@FileListActivity))
                    repository.historyInsertOrUpdate(history)

                    recyclerViewFileList.adapter = adapter
                } else {
                    binding.shimmerFrameLayout.visibility = GONE
                    Snackbar.make(view, "The response is failed", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
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
            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(this)
            }
            finishAffinity()
        }

        inflate.findViewById<TextView>(R.id.see_saved_files)?.setOnClickListener {
            dialog.dismiss()
            Intent(this, SavedFilesActivity::class.java).apply {
                startActivity(this)
            }
        }

        inflate.findViewById<TextView>(R.id.cancel_bottom_sheet)?.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.startShimmer()
    }

    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmer()
        super.onPause()
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
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(this)
                }
                finishAffinity()
                return true
            }

            R.id.nav_share_app -> {
                Constants.showComingSoonToast(this@FileListActivity)
                return true
            }

            R.id.nav_search_web -> {
                Constants.showComingSoonToast(this@FileListActivity)
                return true
            }

            R.id.go_to_saved_file -> {
                Intent(this, SavedFilesActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(this)
                }
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}