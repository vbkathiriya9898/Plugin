package com.example.myapplication

import android.content.Intent
import android.database.Cursor
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    var recorder: MediaRecorder? = null
    var audiofile: File? = null

    //superbderrick.github.io.summerplayerview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        record.setOnClickListener {
            val mediaFile = File(
                Environment.getExternalStorageDirectory().getAbsolutePath()
                    .toString() + "/myvideo.mp4"
            )

            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val photoURI = FileProvider.getUriForFile(
                this,
                applicationContext.getPackageName().toString() + ".provider",
                mediaFile
            )

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                var cursor: Cursor? = null

                try {
                    val proj = arrayOf(MediaStore.Images.Media.DATA)
                    cursor =
                        data?.data?.let { contentResolver.query(it, proj, null, null, null) }
                    val column_index: Int? =
                        cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor?.moveToFirst()
                    val path = column_index?.let { cursor?.getString(it) }
                    Log.e("Path", "" + path)
                    val intent = Intent(this, VideoActivity::class.java)
                    intent.putExtra("Path", path)
                    startActivity(intent)
                } finally {
                    cursor?.close()
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    this, "Video recording cancelled.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this, "Failed to record video",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}