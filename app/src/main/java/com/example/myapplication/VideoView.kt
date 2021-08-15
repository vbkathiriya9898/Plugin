package com.example.myapplication

import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class VideoView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)


        try {
            val file = File(intent.getStringExtra("Path"))

            // Copy file to temporary file in order to view it.
            val temporaryFile = generateTemporaryFile(file.name)
            copy(file, temporaryFile as File)
            videoView.setVideoPath(temporaryFile.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        videoView.requestFocus()
        videoView.start()
    }

    @Throws(IOException::class)
    fun copy(src: File, dst: File) {
        FileInputStream(src).use { `in` ->
            FileOutputStream(dst).use { out ->
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int = 0
                while (`in`.read(buf).also({ len = it }) > 0) {
                    out.write(buf, 0, len)
                }
            }
        }
    }

    private fun generateTemporaryFile(name: String): Any {
        val tempFileName = "20130318_010530_"

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            tempFileName,  /* prefix     "20130318_010530" */
            name,  /* filename   "video.3gp" */
            storageDir /* directory  "/data/sdcard/..." */
        )
    }
}