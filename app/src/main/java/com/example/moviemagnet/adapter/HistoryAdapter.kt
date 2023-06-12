package com.example.moviemagnet.adapter

import android.annotation.*
import android.content.*
import android.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.ui.activity.*
import com.google.android.material.snackbar.*

@SuppressLint("NotifyDataSetChanged")
class HistoryAdapter(history: LiveData<List<HistoryModel>>, private val context: Context) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var fileHistory: List<HistoryModel> = emptyList()
    private lateinit var binding: HistoryItemBinding

    init {
        history.observeForever { updatedFiles ->
            fileHistory = updatedFiles
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryModel) {
            binding.historyTxt.text = history.queryName
            binding.historyType.text = history.queryType
            binding.searchIcon.setOnClickListener {
                if (binding.historyTxt.text != "") {
                    val intent = Intent(context, FileListActivity::class.java)
                    intent.putExtra("query_name", history.queryName)
                    intent.putExtra("type_of_single_file_selected", history.queryType)
                    context.startActivity(intent)
                } else {
                    Snackbar.make(binding.root, "File name must not be empty", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val history = fileHistory[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return fileHistory.size
    }
}