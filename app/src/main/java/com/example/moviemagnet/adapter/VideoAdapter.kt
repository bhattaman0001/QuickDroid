package com.example.moviemagnet.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemagnet.databinding.ItemVideoBinding
import com.example.moviemagnet.model.Video
import com.example.moviemagnet.ui.activity.VideoPlayerActivity

class VideoAdapter(private val context: Context, private val videos: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private lateinit var binding: ItemVideoBinding

    inner class ViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            /*Glide.with(binding.root).load(video.thumbnailPath).into(binding.actionImage)*/
            binding.root.setOnClickListener {
                val intent = Intent(context, VideoPlayerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("path", video.path)
                context.startActivity(intent)
            }
            binding.actionImage.setImageBitmap(video.image)
            binding.title.text = video.title
            binding.size.text = video.size
            binding.lastModified.text = video.lastModified
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.ViewHolder {
        binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoAdapter.ViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)
    }

    override fun getItemCount(): Int {
        return videos.size
    }


}