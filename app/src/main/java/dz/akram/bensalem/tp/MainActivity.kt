package dz.akram.bensalem.tp

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import dz.akram.bensalem.tp.NotificationUtils.ACTION_STOP


class MainActivity : AppCompatActivity() {

    private var mediaPlayer = MediaPlayer()
    private var isPlaying = false
    private lateinit var playPauseIcon : ImageButton
    private lateinit var titleSong : TextView


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            Log.d("MainActivityAkram", "data uri: $uri")

            uri?.let{
                createMediaPlayer(uri = it)
                val title =  getTitleSong(uri = it)
                titleSong.text = title
                Log.d("MainActivityAkram", "title: $title")
            }

        } else {
            Log.d("MainActivityAkram", "result code: ${result.resultCode}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playPauseIcon = findViewById(R.id.play_pause)
        titleSong = findViewById(R.id.title_song)


        playPauseIcon.setOnClickListener {
            if(isPlaying){
                pauseMusic()
            }else{
                playMusic()
            }
        }


        pickAudioIntent()

    }


    // pick audio intent
    private fun pickAudioIntent() {
        try {
            val intent =  Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            )
            resultLauncher.launch(intent)
        } catch (e : Exception) {
            e.printStackTrace()
            Log.d("MainActivityAkram", "Error while picking audio + ${e.message}")
        }
    }


    // create the mediaPlayer
    private fun createMediaPlayer(uri: Uri) {
        try {
            mediaPlayer = MediaPlayer.create(this, uri)
            mediaPlayer.setOnCompletionListener {
                stopMediaService()
                playPauseIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                isPlaying = false
            }
        } catch (e : Exception) {
            e.printStackTrace()
            Log.d("MainActivityAkram", "Error while creating mediaPlayer + ${e.message}")
        }
    }


    // get Title song from mediaPlayer
    private fun getTitleSong(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val pos = cursor?.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE)

        var title = ""

        if (pos != null && pos >= 0) {
            title = cursor.getString(pos)
        }
        cursor?.close()
        return title
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    // play on background
    private fun startMediaService(){
        // start MediaService
        val intent = Intent(this, MediaService::class.java)
        intent.action = titleSong.text.toString()
        startService(intent)

    }

    // stop on background
    private fun stopMediaService(){
        val intent = Intent(this, MediaService::class.java)
        intent.action = ACTION_STOP
        startService(intent)
    }


    // play music
    private fun playMusic() {
        isPlaying = true
        playPauseIcon.setImageResource(R.drawable.ic_round_pause_circle_outline_24)
        mediaPlayer.start()

        startMediaService()
    }

    // pause music
    private fun pauseMusic() {
        isPlaying = false
        playPauseIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
        mediaPlayer.pause()

    }

}