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
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.util.*

@SuppressLint("NotifyDataSetChanged")
class SavedFileAdapter(fileData: LiveData<List<ResponseModel>>, private val context: Context, private val onDeleteClickListener: OnDeleteClickListener) :
    RecyclerView.Adapter<SavedFileAdapter.ViewHolder>() {

    private var files: List<ResponseModel> = emptyList()
    private lateinit var binding: FileItemBinding

    init {
        fileData.observeForever { updatedFiles ->
            files = updatedFiles
            notifyDataSetChanged()
        }
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(file: ResponseModel)
    }

    inner class ViewHolder(private val binding: FileItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(file: ResponseModel, position: Int) {
            binding.nameOfFile.text = file.file_name
            binding.fileType.text = file.file_type
            binding.timeAgo.text = file.time_ago
            binding.dateAdded.text = file.date_added
            binding.fileSize.text = if (file.file_size != "") file.file_size else "No Size"
            val url = file.file_link
            binding.downloadLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }
            binding.numberId.text = "${position + 1}"
            binding.saveYourFile.visibility = GONE
            binding.deleteYourFile.setOnClickListener {
                onDeleteClickListener.onDeleteClick(file)
                Constants.showDeleteToast(context)
            }
            binding.shareFile.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.message + url)
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
            binding.whatsappShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.setPackage("com.whatsapp")
                shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.message + url)
                try {
                    context.startActivity(shareIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Whatsapp is not installed", Toast.LENGTH_SHORT).show()
                }
            }
            binding.instagramShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.message + url)
                shareIntent.setPackage("com.instagram.android")
                try {
                    context.startActivity(shareIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Instagram is not installed", Toast.LENGTH_SHORT).show()
                }
            }
            binding.telegramShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.message + url)
                shareIntent.setPackage("org.telegram.messenger")
                try {
                    context.startActivity(shareIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Telegram is not installed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedFileAdapter.ViewHolder {
        binding = FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedFileAdapter.ViewHolder, position: Int) {
        val file = files[position]
        holder.bind(file, position)
    }

    override fun getItemCount(): Int {
        return files.size
    }
}