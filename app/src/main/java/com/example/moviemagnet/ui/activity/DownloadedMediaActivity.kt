package com.example.moviemagnet.ui.activity

import android.content.pm.PackageManager
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemagnet.adapter.VideoAdapter
import com.example.moviemagnet.databinding.ActivityDownloadedMediaBinding
import com.example.moviemagnet.model.Video
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DownloadedMediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadedMediaBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadedMediaBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        recyclerView = binding.downloadListRv
        recyclerView.layoutManager = LinearLayoutManager(this)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            PackageManager.PERMISSION_GRANTED
        )
        val videos = getDownloadedVideos()
        if (videos.isEmpty()) {
            binding.noDownloaded.visibility = VISIBLE
            binding.downloadListRv.visibility = GONE
        } else {
            binding.noDownloaded.visibility = GONE
            binding.downloadListRv.visibility = VISIBLE
            videoAdapter = VideoAdapter(videos)
            recyclerView.adapter = videoAdapter
        }
    }

    private fun getDownloadedVideos(): List<Video> {
        val downloadsFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val files: Array<File> = downloadsFolder.listFiles()!!
        val videos: MutableList<Video> = mutableListOf()
        files.let {
            for (file in it) {
                if (isVideoFile(file.extension)) {
                    val size = getFileSizeInGB(file)
                    val lastModified = getFileLastModifiedDateTime(file)
                    val bMap = ThumbnailUtils.createVideoThumbnail(
                        file.absolutePath, MediaStore.Video.Thumbnails.MICRO_KIND
                    )
                    val video = Video(
                        file.name,
                        bMap,
                        lastModified,
                        size
                    )
                    videos.add(video)
                }
            }
        }

        return videos
    }

    /*private fun getVideoThumbnailPath(videoPath: String): String? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)
        val thumbnail = retriever.getFrameAtTime(1) // Get the thumbnail at the first second
        val thumbnailPath = thumbnail?.let { saveThumbnail(it) }
        retriever.release()
        return thumbnailPath
    }

    private fun saveThumbnail(thumbnail: Bitmap): String {
        val thumbnailFile = File.createTempFile("thumbnail", ".jpg")
        val outputStream = FileOutputStream(thumbnailFile)
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        return thumbnailFile.absolutePath
    }*/

    private fun isVideoFile(extension: String): Boolean {
        return when (extension.toLowerCase(Locale.ROOT)) {
            "mp4", "avi", "mkv", "wmv", "mov", "flv", "webm", "mpeg", "mpg" -> true
            else -> false
        }
    }

    private fun getFileLastModifiedDateTime(file: File): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val lastModified = Date(file.lastModified())
        return dateFormat.format(lastModified)
    }

    private fun getFileSizeInGB(file: File): String {
        val fileSizeInBytes = file.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        val fileSizeInMB = fileSizeInKB / 1024
        val fileSizeInGB = fileSizeInMB / 1024.0
        return "%.2f GB".format(fileSizeInGB)
    }

}