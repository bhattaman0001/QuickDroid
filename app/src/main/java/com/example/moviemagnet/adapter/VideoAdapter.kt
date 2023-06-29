package com.example.moviemagnet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemagnet.databinding.ItemVideoBinding
import com.example.moviemagnet.model.Video

class VideoAdapter(private val videos: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private lateinit var binding: ItemVideoBinding

    inner class ViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            /*Glide.with(binding.root).load(video.thumbnailPath).into(binding.actionImage)*/
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