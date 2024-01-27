package com.example.moviemagnet.ui.adapter

import android.annotation.*
import android.content.*
import android.net.*
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.model.ResponseModel
import com.example.moviemagnet.data.repository.Repository
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.util.*
import kotlinx.coroutines.*

class FileResponseAdapter(
    private val data: List<ResponseModel>?,
    private val context: Context,
    private val repository: Repository
) :
    RecyclerView.Adapter<FileResponseAdapter.ViewHolder>() {

    private lateinit var binding: FileItemBinding
    private var isVisibleB = false

    fun setButtonVisibility(isVisible: Boolean){
        this.isVisibleB = isVisible
    }

    inner class ViewHolder(private val binding: FileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(response: ResponseModel?, position: Int) {
            binding.nameOfFile.text = response?.file_name
            binding.fileType.text = response?.file_type
            binding.dateAdded.text = response?.date_added
            binding.timeAgo.text = response?.time_ago
            binding.fileSize.text =
                if (response?.file_size != "") response?.file_size else "No Size"
            val url = response?.file_link
            binding.downloadLink.setOnClickListener {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                    context.startActivity(this)
                }
            }
            binding.numberId.text = "${position + 1}"
            binding.saveYourFile.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    response?.let { it1 -> repository.insertOrUpdateSavedFiles(it1) }
                }
                binding.saveYourFile.background.setTint(context.resources.getColor(android.R.color.holo_green_light))
                binding.saveYourFile.setTextColor(context.resources.getColor(R.color.black))
                Constants.showInsertToast(context)
                binding.saveYourFile.isEnabled = isVisibleB
                binding.saveYourFile.text = "Item Saved"
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != -1) {
            val file_found_are = data?.get(position)
            holder.bind(file_found_are, position)
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: -1
    }

}