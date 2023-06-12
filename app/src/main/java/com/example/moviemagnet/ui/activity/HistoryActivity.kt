package com.example.moviemagnet.ui.activity

import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.adapter.*
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.db.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyFileAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        val historyLiveData = HistoryDatabase(this).getHistoryDao().getAllHistory()
        historyLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.rvHistorySearch.visibility = View.INVISIBLE
                binding.noHistorySearch.visibility = View.VISIBLE
            } else {
                binding.rvHistorySearch.visibility = View.VISIBLE
                binding.noHistorySearch.visibility = View.INVISIBLE
                recyclerView = binding.rvHistorySearch
                recyclerView.layoutManager = LinearLayoutManager(this)
                historyFileAdapter = HistoryAdapter(historyLiveData, this)
                recyclerView.adapter = historyFileAdapter
            }
        }
    }
}