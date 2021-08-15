package com.example.myapplication

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.os.Environment
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class VideoActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private var duration: String? = ""
    private var stopPosition: Int = 0
    lateinit var videoActivity: VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)

        videoActivity = findViewById<VideoView>(R.id.videoView)


        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoActivity)
        videoActivity.setMediaController(mediaController)


        try {
            val file = File(intent.getStringExtra("Path"))

            // Copy file to temporary file in order to view it.
            val temporaryFile = generateTemporaryFile(file.name)
            copy(file, temporaryFile as File)

            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(temporaryFile.absolutePath);
            duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            retriever.release()

            videoActivity.setVideoPath(temporaryFile.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        videoActivity.requestFocus()
        videoActivity.start()

    }

    override fun onResume() {
        super.onResume()
        videoActivity.seekTo(stopPosition)
//        videoActivity.resume()
    }

    override fun onPause() {
        super.onPause()
        stopPosition = videoActivity.currentPosition; //stopPosition is an int
//        videoView.pause()
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

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            if (fromUser) {
                // Mean that the seekbar value is changed by user
                videoView.seekTo((progress * (duration!!.toInt()) / 100)) // Verify this
            } else {
                // Ignore becuase is due to seekBar programmatically change
            }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}