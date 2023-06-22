package com.example.moviemagnet.adapter

import android.annotation.*
import android.content.*
import android.net.*
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.databinding.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.repository.*
import com.example.moviemagnet.util.*
import kotlinx.coroutines.*


class FileResponseAdapter(private val data: List<ResponseModel>?, private val context: Context, private val repository: Repository) :
    RecyclerView.Adapter<FileResponseAdapter.ViewHolder>() {

    private lateinit var binding: FileItemBinding

    inner class ViewHolder(private val binding: FileItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(response: ResponseModel?, position: Int) {
            binding.nameOfFile.text = response?.file_name
            binding.fileType.text = response?.file_type
            binding.dateAdded.text = response?.date_added
            binding.timeAgo.text = response?.time_ago
            binding.fileSize.text = if (response?.file_size != "") response?.file_size else "No Size"
            val url = response?.file_link
            binding.downloadLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }
            binding.numberId.text = "${position + 1}"
            binding.saveYourFile.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    response?.let { it1 -> repository.responseInsertOrUpdate(it1) }
                }
                binding.saveYourFile.text = "Saved"
                binding.saveYourFile.background.setTint(context.resources.getColor(android.R.color.holo_green_light))
                binding.saveYourFile.setTextColor(context.resources.getColor(R.color.black))
                Constants.showInsertToast(context)
            }
            binding.deleteYourFile.visibility = GONE
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileResponseAdapter.ViewHolder {
        binding = FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FileResponseAdapter.ViewHolder, position: Int) {
        if (position != -1) {
            val file_found_are = data?.get(position)
            holder.bind(file_found_are, position)
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: -1
    }

}