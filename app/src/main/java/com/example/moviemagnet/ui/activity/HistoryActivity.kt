package com.example.moviemagnet.ui.activity

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.adapter.*
import com.example.moviemagnet.database.*
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.HistoryModel
import com.example.moviemagnet.repository.*
import com.example.moviemagnet.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity(), HistoryAdapter.OnDeleteClickListener {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyFileAdapter: HistoryAdapter
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        repository = Repository(HistoryDatabase(this), null)
        val historyLiveData = repository.historyGetAllHistory()
        historyLiveData?.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.rvHistorySearch.visibility = View.INVISIBLE
                binding.noHistorySearch.visibility = View.VISIBLE
            } else {
                binding.rvHistorySearch.visibility = View.VISIBLE
                binding.noHistorySearch.visibility = View.INVISIBLE
                recyclerView = binding.rvHistorySearch
                recyclerView.layoutManager = LinearLayoutManager(this)
                historyFileAdapter = HistoryAdapter(historyLiveData, this, this)
                recyclerView.adapter = historyFileAdapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu?.findItem(R.id.nav_home)
        item?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.clear_all -> {
                Constants.deleteAllHistory(this@HistoryActivity)
                Toast.makeText(this, "Delete all saved files successfully", Toast.LENGTH_SHORT)
                    .show()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDeleteClick(history: HistoryModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.historyDelete(history)
        }
    }
}