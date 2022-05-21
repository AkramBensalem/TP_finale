package dz.akram.bensalem.tp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import dz.akram.bensalem.tp.database.Song
import dz.akram.bensalem.tp.utils.Constants
import dz.akram.bensalem.tp.utils.Constants.ACTION_MUSIC_CLICKED
import dz.akram.bensalem.tp.utils.Constants.ACTION_PAUSE
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY_NEXT_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY_PREVIOUS_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_REWIND
import dz.akram.bensalem.tp.utils.Constants.ACTION_SKIP_NEXT
import dz.akram.bensalem.tp.utils.Constants.ACTION_SKIP_PREVIOUS
import dz.akram.bensalem.tp.utils.Constants.ACTION_SOME_INTENT
import dz.akram.bensalem.tp.utils.Constants.ACTION_STOP
import dz.akram.bensalem.tp.utils.Constants.ACTION_TOGGLE_PLAYBACK
import dz.akram.bensalem.tp.utils.Constants.ACTION_TOGGLE_PLAY_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.EXTRA_DATA
import dz.akram.bensalem.tp.utils.Constants.EXTRA_SONG

class NotificationReceiver : BroadcastReceiver() {

    private fun sendToMediaServiceWithAction(
        context: Context?,
        action: String,
        song: Song? = null
    ) {
        val intent = Intent(context, MediaService::class.java)
        intent.action = action
        if (song != null) {
            intent.putExtra(EXTRA_SONG, song)
        }
        context?.startService(intent)
    }

    private fun sendBroadcastToMainActivityWithAction(
        context: Context?,
        action: String
    ){
        val broadcastIntent = Intent()
        broadcastIntent.action = action
        context?.sendBroadcast(broadcastIntent)
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action
        val song = intent?.extras?.getParcelable(EXTRA_SONG) as Song?


        action?.apply {
            when (action) {
                ACTION_PLAY -> sendToMediaServiceWithAction(context, ACTION_PLAY, song = song)
                ACTION_PAUSE -> sendToMediaServiceWithAction(context, ACTION_PAUSE, song = song)
                ACTION_STOP -> sendToMediaServiceWithAction(context, ACTION_STOP)
                ACTION_SKIP_NEXT -> sendToMediaServiceWithAction(context, ACTION_SKIP_NEXT, song = song)
                ACTION_SKIP_PREVIOUS -> sendToMediaServiceWithAction(context, ACTION_SKIP_PREVIOUS, song = song)
                ACTION_TOGGLE_PLAYBACK -> {
                    sendToMediaServiceWithAction(context, ACTION_TOGGLE_PLAYBACK, song = song)
                }
                ACTION_MUSIC_CLICKED -> sendToMediaServiceWithAction(context, ACTION_MUSIC_CLICKED, song = song)

                ACTION_PLAY_NEXT_NOTIFICATION -> {
                    sendBroadcastToMainActivityWithAction(context, ACTION_PLAY_NEXT_NOTIFICATION)
                }
                ACTION_TOGGLE_PLAY_NOTIFICATION -> {
                    sendBroadcastToMainActivityWithAction(context, ACTION_TOGGLE_PLAY_NOTIFICATION)
                }
                ACTION_PLAY_PREVIOUS_NOTIFICATION -> {
                    sendBroadcastToMainActivityWithAction(context, ACTION_PLAY_PREVIOUS_NOTIFICATION)
                }
            }


            }
    }

}
