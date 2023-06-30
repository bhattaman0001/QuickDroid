package com.example.moviemagnet.ui.activity

import android.content.*
import android.os.*
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.data.db.database.SavedFileRoomDatabase
import com.example.moviemagnet.data.db.entity.ResponseModel
import com.example.moviemagnet.data.repository.Repository
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.ui.adapter.SavedFileAdapter
import com.example.moviemagnet.util.*
import kotlinx.coroutines.*

class SavedFilesActivity : AppCompatActivity(), SavedFileAdapter.OnDeleteClickListener {
    private lateinit var binding: ActivitySavedFilesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var savedFileAdapter: SavedFileAdapter
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedFilesBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        repository = Repository(SavedFileRoomDatabase(this))
        val fileLiveData = repository.responseGetAllFile()
        recyclerView = binding.rvSavedFile
        fileLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.rvSavedFile.visibility = GONE
                binding.noSavedFile.visibility = VISIBLE
            } else {
                binding.rvSavedFile.visibility = VISIBLE
                binding.noSavedFile.visibility = GONE
                recyclerView.layoutManager = LinearLayoutManager(this)
                savedFileAdapter = SavedFileAdapter(fileLiveData, this, this)
                recyclerView.adapter = savedFileAdapter
            }
            binding.progressBar.visibility = GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(this)
                }
                finishAffinity()
                return true
            }

            R.id.clear_all -> {
                Constants.deleteAllSavedFile(this@SavedFilesActivity)
                Toast.makeText(this, "Delete all saved files successfully", Toast.LENGTH_SHORT)
                    .show()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDeleteClick(file: ResponseModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.responseDeleteFile(file)
        }
    }
}