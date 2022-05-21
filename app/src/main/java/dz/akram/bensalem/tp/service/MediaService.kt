package dz.akram.bensalem.tp.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import dz.akram.bensalem.tp.database.Song
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_MUSIC_CLICKED
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_PAUSE
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_PLAY
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_SKIP_NEXT
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_SKIP_PREVIOUS
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_STOP
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_TOGGLE_PLAYBACK
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_UI_PAUSE
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_UI_PLAY
import dz.akram.bensalem.tp.utils.Constants.ACTION_MUSIC_CLICKED
import dz.akram.bensalem.tp.utils.Constants.ACTION_PAUSE
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY
import dz.akram.bensalem.tp.utils.Constants.ACTION_SKIP_NEXT
import dz.akram.bensalem.tp.utils.Constants.ACTION_SKIP_PREVIOUS
import dz.akram.bensalem.tp.utils.Constants.ACTION_STOP
import dz.akram.bensalem.tp.utils.Constants.ACTION_TOGGLE_PLAYBACK
import dz.akram.bensalem.tp.utils.Constants.ACTION_UI_PAUSE
import dz.akram.bensalem.tp.utils.Constants.ACTION_UI_PLAY
import dz.akram.bensalem.tp.utils.Constants.EXTRA_SONG
import dz.akram.bensalem.tp.utils.Constants.NOTIFICATION_ID
import dz.akram.bensalem.tp.utils.NotificationUtils


class MediaService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {

  //  private var currentSongUri: Uri? = null
    private var currentSong: Song? = null


    private var isFromMainActivity = true

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MediaService = this@MediaService
    }

    // Our media player
    private var mPlayer: MediaPlayer? = null


    override fun onBind(intent: Intent): IBinder {
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val action = intent?.action

        action?.let { it ->
            currentSong = intent.getParcelableExtra(EXTRA_SONG)
            isFromMainActivity = !it.contains("favourite")

            when (it) {
                ACTION_MUSIC_CLICKED, ACTION_FAVOURITE_MUSIC_CLICKED -> {
                    // User clicked on the notification - open the app
                    currentSong?.let { song ->
                        configMediaPlayer(song)
                    }
                }

                ACTION_TOGGLE_PLAYBACK, ACTION_FAVOURITE_TOGGLE_PLAYBACK -> {
                    //
                    currentSong?.let { song ->
                        if (mPlayer?.isPlaying == true) {
                            pauseSong()
                        } else {
                            playSong()
                        }
                    }

                }
                ACTION_PLAY, ACTION_FAVOURITE_PLAY -> {
                    //
                    playSong()
                }
                ACTION_PAUSE, ACTION_FAVOURITE_PAUSE -> {
                    //
                    pauseSong()
                }
                ACTION_SKIP_NEXT, ACTION_SKIP_PREVIOUS, ACTION_FAVOURITE_SKIP_NEXT, ACTION_FAVOURITE_SKIP_PREVIOUS -> {
                    //
                    currentSong?.let { song ->
                        playNextOrPreviousSong()
                    }
                }
                ACTION_STOP, ACTION_FAVOURITE_STOP -> {
                    //
                    releaseResources(state = true)
                    stopForeground(true)
                }
                else -> {}
            }
        }

        return START_NOT_STICKY
    }


    private fun pauseSong() {
        mPlayer?.let {
            if (it.isPlaying) {



                val broadcastIntent = Intent()
                broadcastIntent.action =  if (isFromMainActivity) ACTION_UI_PAUSE else ACTION_FAVOURITE_UI_PAUSE
                sendBroadcast(broadcastIntent)


                currentSong?.let { s ->
                    startForeground(NOTIFICATION_ID, buildNotification(s))
                }

                it.pause()
            }

        }

    }

    private fun playSong() {

        mPlayer?.apply {
            if (!this.isPlaying) {
                val broadcastIntent = Intent()
                broadcastIntent.action = if (isFromMainActivity) ACTION_UI_PLAY else ACTION_FAVOURITE_UI_PLAY
                sendBroadcast(broadcastIntent)

                currentSong?.let {
                    startForeground(NOTIFICATION_ID, buildNotification(it))
                }

                start()
            }
        } ?: run {
            currentSong?.let { uri ->
                configMediaPlayer(uri)
            }
        }

    }


    private fun playNextOrPreviousSong() {
            currentSong?.let { uri ->
                configMediaPlayer(uri)
        }
    }


    // show notification
    private fun buildNotification(
        song: Song
    ): Notification = NotificationUtils.createNotification(
            activity = this,
            song = song.name,
            isPlaying = mPlayer?.isPlaying ?: true,
            isFromMainActivity = isFromMainActivity
        ).build()



    private fun releaseResources(state: Boolean) {
        stopForeground(false)
        if (state) {
            mPlayer?.let {
                it.reset()
                it.release()
                mPlayer = null
            }
        }
    }




    override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean {
        Toast.makeText(this, "Media player  what=$what, extra=$extra", Toast.LENGTH_SHORT).show()

        val broadcastIntent = Intent()
        broadcastIntent.action =  if (isFromMainActivity) ACTION_UI_PAUSE else ACTION_FAVOURITE_UI_PAUSE
        sendBroadcast(broadcastIntent)



        releaseResources(state = true)

        stopForeground(true)

        return true
    }

    override fun onCompletion(mediaPlayer: MediaPlayer?) {

        val broadcastIntent = Intent()
        broadcastIntent.action =   if (isFromMainActivity) ACTION_UI_PAUSE else ACTION_FAVOURITE_UI_PAUSE
        sendBroadcast(broadcastIntent)

        releaseResources(state = true)

        currentSong?.let {
            startForeground(NOTIFICATION_ID, buildNotification(it))
        }
    }



    override fun onPrepared(mediaPlayer: MediaPlayer?) {

        val broadcastIntent = Intent()
        broadcastIntent.action =if (isFromMainActivity) ACTION_UI_PLAY else ACTION_FAVOURITE_UI_PLAY
        sendBroadcast(broadcastIntent)
        currentSong?.let {
            startForeground(NOTIFICATION_ID, buildNotification(it))
        }

        mediaPlayer?.start()

    }


    private fun configMediaPlayer(s: Song) {
         createMediaPlayerIfNeeded(s)
    }

    private fun createMediaPlayerIfNeeded(
        s : Song
    ) {
        releaseResources(state = true)
        mPlayer = MediaPlayer()
        mPlayer?.let{

                it.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )

                it.setDataSource(this@MediaService, Uri.parse(s.path))



                it.prepareAsync()


                it.setOnErrorListener(this)
                it.setOnCompletionListener(this)
                it.setOnPreparedListener(this)

        }

    }



    override fun onDestroy() {
        super.onDestroy()
        releaseResources(true)
    }


}