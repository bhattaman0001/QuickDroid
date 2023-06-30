package com.example.moviemagnet.ui.adapter

import android.annotation.*
import android.app.AlertDialog
import android.content.*
import android.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.ui.activity.*
import com.google.android.material.snackbar.*
import com.example.moviemagnet.data.db.entity.HistoryModel


@SuppressLint("NotifyDataSetChanged")
class HistoryAdapter(
    history: LiveData<List<HistoryModel>>,
    private val context: Context,
    private val onDeleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var fileHistory: List<HistoryModel> = emptyList()
    private lateinit var binding: HistoryItemBinding

    init {
        history.observeForever { updatedFiles ->
            fileHistory = updatedFiles
            notifyDataSetChanged()
        }
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(history: HistoryModel)
    }

    inner class ViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnLongClickListener {

        init {
            binding.root.setOnLongClickListener(this)
        }

        private lateinit var history: HistoryModel

        fun bind(history: HistoryModel) {
            this.history = history
            binding.historyTxt.text = history.queryName
            binding.historyType.text = history.queryType
            binding.searchIcon.setOnClickListener {
                if (binding.historyTxt.text != "") {
                    Intent(context, FileListActivity::class.java).apply {
                        putExtra("query_name", history.queryName)
                        putExtra("type_of_single_file_selected", history.queryType)
                        context.startActivity(this)
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        "File name must not be empty",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        override fun onLongClick(view: View?): Boolean {
            AlertDialog.Builder(context)
                .setTitle("Delete History")
                .setMessage("Are you sure you want to delete it?")
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ ->
                    onDeleteClickListener.onDeleteClick(history)
                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = fileHistory[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return fileHistory.size
    }
}