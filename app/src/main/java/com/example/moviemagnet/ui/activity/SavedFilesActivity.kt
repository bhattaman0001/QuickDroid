package com.example.moviemagnet.ui.activity

import android.content.*
import android.os.*
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.adapter.*
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.db.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.util.*

class SavedFilesActivity : AppCompatActivity(), SavedFileAdapter.OnDeleteClickListener {
    private lateinit var binding: ActivitySavedFilesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var savedFileAdapter: SavedFileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedFilesBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        val fileLiveData = SavedFileRoomDatabase(this).getFileDaa().getAllFile()
        recyclerView = binding.rvSavedFile
        fileLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.rvSavedFile.visibility = INVISIBLE
                binding.noSavedFile.visibility = VISIBLE
            } else {
                binding.rvSavedFile.visibility = VISIBLE
                binding.noSavedFile.visibility = INVISIBLE
                recyclerView.layoutManager = LinearLayoutManager(this)
                savedFileAdapter = SavedFileAdapter(fileLiveData, this, this)
                recyclerView.adapter = savedFileAdapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finishAffinity()
                return true
            }

            R.id.delete_db_entries -> {
                Util.deleteAllFile(this@SavedFilesActivity)
                Toast.makeText(this, "Delete all saved files successfully", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDeleteClick(file: ResponseModel) {
        Util.deleteFile(this@SavedFilesActivity, file)
    }
}