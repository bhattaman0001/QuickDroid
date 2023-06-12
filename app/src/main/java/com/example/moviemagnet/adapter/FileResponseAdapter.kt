package com.example.moviemagnet.adapter

import android.annotation.*
import android.content.*
import android.net.*
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.recyclerview.widget.*
import com.example.moviemagnet.*
import com.example.moviemagnet.model.*
import com.example.moviemagnet.util.*


class FileResponseAdapter(private val data: List<ResponseModel>?, private val context: Context) : RecyclerView.Adapter<FileResponseAdapter.ViewHolder>() {

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
        var ultimate_share: ImageView = itemView.findViewById(R.id.share_file),
        var whatsapp_share: ImageView = itemView.findViewById(R.id.whatsapp_share),
        var instagram_share: ImageView = itemView.findViewById(R.id.instagram_share),
        var telegram_share: ImageView = itemView.findViewById(R.id.telegram_share),
    ) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileResponseAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout._file_item, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FileResponseAdapter.ViewHolder, position: Int) {
        if (position != -1) {
            val file_found_are = data?.get(position)
            holder.name_of_file.text = file_found_are?.file_name
            holder.file_type.text = file_found_are?.file_type
            holder.date_added.text = file_found_are?.date_added
            holder.time_ago.text = file_found_are?.time_ago
            holder.file_size.text = if (file_found_are?.file_size != "") file_found_are?.file_size else "No Size"
            val url = file_found_are?.file_link
            holder.download_link.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }
            holder.number_id.text = "${position + 1}"
            holder.save_your_file.setOnClickListener {
                file_found_are?.let { it1 -> Util.insertOrUpdate(it1, context) }
                holder.save_your_file.text = "Saved"
                holder.save_your_file.background.setTint(context.resources.getColor(android.R.color.holo_green_light))
                holder.save_your_file.setTextColor(context.resources.getColor(R.color.black))
                Util.showInsertToast(context)
            }
            holder.delete_your_file.visibility = GONE
            holder.ultimate_share.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, Util.message + url)
                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
            holder.whatsapp_share.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.setPackage("com.whatsapp")
                shareIntent.putExtra(Intent.EXTRA_TEXT, Util.message + url)
                try {
                    context.startActivity(shareIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Whatsapp is not installed", Toast.LENGTH_SHORT).show()
                }
            }
            holder.instagram_share.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, Util.message + url)
                shareIntent.setPackage("com.instagram.android")
                try {
                    context.startActivity(shareIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Instagram is not installed", Toast.LENGTH_SHORT).show()
                }
            }
            holder.telegram_share.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, Util.message + url)
                shareIntent.setPackage("org.telegram.messenger")
                try {
                    context.startActivity(shareIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context, "Telegram is not installed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: -1
    }

}