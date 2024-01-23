package com.example.moviemagnet.ui.adapter

import android.annotation.*
import android.content.*
import android.net.*
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.model.ResponseModel
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.util.*

@SuppressLint("NotifyDataSetChanged")
class SavedFileAdapter(
    fileData: LiveData<List<ResponseModel>>,
    private val context: Context,
    private val clickListener: onClickListener
) : RecyclerView.Adapter<SavedFileAdapter.ViewHolder>() {

    private var files: List<ResponseModel> = emptyList()
    private lateinit var binding: FileItemBinding

    init {
        fileData.observeForever { updatedFiles ->
            files = updatedFiles
            notifyDataSetChanged()
        }
    }

    interface onClickListener {
        fun onDeleteClick(file: ResponseModel)
        fun isVisible(isVisible: Boolean)
    }

    inner class ViewHolder(private val binding: FileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(file: ResponseModel, position: Int) {
            binding.nameOfFile.text = file.file_name
            binding.fileType.text = file.file_type
            binding.timeAgo.text = file.time_ago
            binding.dateAdded.text = file.date_added
            binding.fileSize.text = if (file.file_size != "") file.file_size else "No Size"
            val url = file.file_link
            binding.downloadLink.setOnClickListener {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                    context.startActivity(this)
                }
            }
            binding.numberId.text = "${position + 1}"
            binding.saveYourFile.backgroundTintList = ContextCompat.getColorStateList(context, R.color.red)
            binding.saveYourFile.text = "Delete"
            binding.saveYourFile.setOnClickListener {
                clickListener.onDeleteClick(file)
                clickListener.isVisible(true)
                Constants.showDeleteToast(context)
            }
            binding.shareFile.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, Constants.message + url)
                    context.startActivity(Intent.createChooser(this, "Share via"))
                }
            }
            binding.whatsappShare.setOnClickListener {
                try {
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        setPackage("com.whatsapp")
                        putExtra(Intent.EXTRA_TEXT, Constants.message + url)
                        context.startActivity(this)
                    }
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Whatsapp is not installed", Toast.LENGTH_SHORT).show()
                }
            }
            binding.instagramShare.setOnClickListener {
                try {
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, Constants.message + url)
                        setPackage("com.instagram.android")
                        context.startActivity(this)
                    }
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Instagram is not installed", Toast.LENGTH_SHORT).show()
                }
            }
            binding.telegramShare.setOnClickListener {
                try {
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, Constants.message + url)
                        setPackage("org.telegram.messenger")
                        context.startActivity(this)
                    }
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Telegram is not installed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]
        holder.bind(file, position)
    }

    override fun getItemCount(): Int {
        return files.size
    }
}