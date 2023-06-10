package com.example.moviemagnet.adapter

import android.annotation.*
import android.content.*
import android.net.*
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.util.*

@SuppressLint("NotifyDataSetChanged")
class SavedFileAdapter(private val fileData: LiveData<List<ResponseModel>>, private val context: Context, private val onDeleteClickListener: OnDeleteClickListener) :
    RecyclerView.Adapter<SavedFileAdapter.ViewHolder>() {

    private var files: List<ResponseModel> = emptyList()

    init {
        fileData.observeForever { updatedFiles ->
            files = updatedFiles
            notifyDataSetChanged()
        }
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(file: ResponseModel)
    }

    inner class ViewHolder(
        itemView: View,
        var name_of_file: TextView = itemView.findViewById(R.id.name_of_file),
        var file_type: TextView = itemView.findViewById(R.id.file_type),
        var date_added: TextView = itemView.findViewById(R.id.date_added),
        var time_ago: TextView = itemView.findViewById(R.id.time_ago),
        var file_size: TextView = itemView.findViewById(R.id.file_size),
        var download_link: Button = itemView.findViewById(R.id.download_link),
        var number_id: TextView = itemView.findViewById(R.id.number_id),
        var save_your_file: Button = itemView.findViewById(R.id.save_your_file),
        var delete_your_file: Button = itemView.findViewById(R.id.delete_your_file),
    ) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedFileAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout._file_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SavedFileAdapter.ViewHolder, position: Int) {
        val file = files[position]
        holder.name_of_file.text = file.file_name
        holder.file_type.text = file.file_type
        holder.time_ago.text = file.time_ago
        holder.date_added.text = file.date_added
        holder.file_size.text = file.file_size
        holder.download_link.setOnClickListener {
            val url = file.file_link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
        holder.number_id.text = "${position + 1}"
        holder.save_your_file.visibility = GONE
        holder.delete_your_file.setOnClickListener {
            onDeleteClickListener.onDeleteClick(file)
            Util.showDeleteToast(context)
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }
}