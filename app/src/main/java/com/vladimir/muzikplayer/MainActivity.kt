package com.vladimir.muzikplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


const val REQUEST_CODE = 44

class MainActivity : AppCompatActivity() {

    companion object {
        var mPlayer: MediaPlayer? = null
    }

    private val songUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    private val activity: MainActivity = this
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recyclerView = findViewById(R.id.recycler_view)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        } else {
            initWidgets()
        }

    }

    /**
     * Initializes RecyclerView and it's dependent components
     * Binds data to the recyclerView using a custom adapter.
     */
    private fun initWidgets() {

        val data = getMusicFromStorage()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = MusicAdapter(applicationContext, data, recyclerView)
            setHasFixedSize(true)
        }
    }

    /**
     * reads music files from storage device.
     */
    private fun getMusicFromStorage(): ArrayList<MusicModel> {

        val musicList = ArrayList<MusicModel>()

        val musicCursor = applicationContext.contentResolver.query(songUri, null, null, null, null)

        if (musicCursor != null && musicCursor.moveToFirst()) {
            val songTitle = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songDuration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            } else {
                0
            }
            val songId = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID)

            do {
                val title = musicCursor.getString(songTitle)
                val artist = musicCursor.getString(songArtist)
                val duration = musicCursor.getString(songDuration)
                val id = musicCursor.getString(songId)
                musicList.add(MusicModel(title, artist, duration.toLong(), id))
            } while (musicCursor.moveToNext())

        }
        musicCursor?.close()
        return musicList
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initWidgets()

            } else {
                finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

