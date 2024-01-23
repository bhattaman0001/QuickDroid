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
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.api.*
import com.example.moviemagnet.data.repository.Repository
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.ui.adapter.FileResponseAdapter
import com.example.moviemagnet.ui.viewmodels.MainViewModel
import com.example.moviemagnet.util.*
import com.google.android.material.bottomsheet.*
import com.google.android.material.snackbar.*
import kotlinx.coroutines.*

class FileListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileListBinding
    private lateinit var recyclerViewFileList: RecyclerView
    private lateinit var adapter: FileResponseAdapter
    private lateinit var repository: Repository
    private lateinit var mainViewModel: MainViewModel
    private lateinit var qN: String // queryName
    private lateinit var tOFS: String // typeOfFileSelected
    private lateinit var fApp: FileApplication

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getIntentValues() {
        qN = intent?.getStringExtra("queryName").toString()
        tOFS = intent?.getStringExtra("typeOfFileSelected").toString()
    }

    private fun setUpRecyclerView() {
        recyclerViewFileList.layoutManager = LinearLayoutManager(this@FileListActivity)
        mainViewModel.getRequestResponse(qN, tOFS)
    }

    private fun setUpViewModel() {
        fApp = application as FileApplication
        repository = fApp.hRepo // well here you can use rRepo also
        mainViewModel = ViewModelProvider(this, MainViewModel.Companion.MainViewModelFactory(application, repository))[MainViewModel::class.java]
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

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding = ActivityFileListBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        recyclerViewFileList = binding.fileListRv
        getIntentValues()
        setUpViewModel()
        setUpRecyclerView()
        val history = HistoryModel(qN, tOFS)
        try {
            mainViewModel.responseLiveData.observe(this@FileListActivity) { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.responseTime.text =
                            "Your request took ${response.d2} seconds to find and display! Thanks"
                        binding.responseTime.isAllCaps = true
                        runOnUiThread(Runnable {
                            run {
                                binding.shimmerFrameLayout.stopShimmer()
                                binding.responseTime.visibility = VISIBLE
                                binding.numberOfResults.visibility = VISIBLE
                                binding.shimmerFrameLayout.visibility = GONE
                                recyclerViewFileList.visibility = VISIBLE
                            }
                        })
                        binding.numberOfResults.text = "Total results : " + response.d3
                        binding.numberOfResults.isAllCaps = true

                        repository = fApp.sRepo
                        // adapter is getting initialized
                        adapter = FileResponseAdapter(response.d1?.files_found, this@FileListActivity, repository)

                        // if the response is success then insert it into history db
                        repository = fApp.hRepo
                        CoroutineScope(Dispatchers.IO).launch { repository.historyInsertOrUpdate(history) }

                        // set the adapter to RV
                        recyclerViewFileList.adapter = adapter
                    }

                    is Resource.Error -> {
                        binding.shimmerFrameLayout.visibility = GONE
                        response.message.let {
                            Snackbar.make(view, "The response is failed", Snackbar.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                        binding.shimmerFrameLayout.visibility = VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            Snackbar.make(view, "Open your Internet", Snackbar.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmer()
        super.onPause()
    }

    override fun onRestart() {
        super.onRestart()
        val sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
        val receivedData: Boolean = sharedPreferences.getString("keyDataToSend", "Default Value").toBoolean()
        adapter.setButtonVisibility(receivedData)
    }

    override fun onDestroy() {
        super.onDestroy()
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