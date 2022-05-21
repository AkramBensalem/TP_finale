package dz.akram.bensalem.tp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dz.akram.bensalem.tp.database.Song
import dz.akram.bensalem.tp.utils.Constants

class FavouriteNotificationReceiver : BroadcastReceiver() {

    private fun sendToMediaServiceWithAction(
        context: Context?,
        action: String,
        song: Song? = null
    ) {
        val intent = Intent(context, MediaService::class.java)
        intent.action = action
        if (song != null) {
            intent.putExtra(Constants.EXTRA_SONG, song)
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
        val song = intent?.extras?.getParcelable(Constants.EXTRA_SONG) as Song?


        action?.apply {


            when (action) {
                Constants.ACTION_FAVOURITE_PLAY -> sendToMediaServiceWithAction(context,
                    Constants.ACTION_FAVOURITE_PLAY, song = song)
                Constants.ACTION_FAVOURITE_PAUSE -> sendToMediaServiceWithAction(context,
                    Constants.ACTION_FAVOURITE_PAUSE, song = song)
                Constants.ACTION_FAVOURITE_STOP -> sendToMediaServiceWithAction(context,
                    Constants.ACTION_FAVOURITE_STOP
                )
                Constants.ACTION_FAVOURITE_SKIP_NEXT -> sendToMediaServiceWithAction(context,
                    Constants.ACTION_FAVOURITE_SKIP_NEXT, song = song)
                Constants.ACTION_FAVOURITE_SKIP_PREVIOUS -> sendToMediaServiceWithAction(context,
                    Constants.ACTION_FAVOURITE_SKIP_PREVIOUS, song = song)
                Constants.ACTION_FAVOURITE_TOGGLE_PLAYBACK -> {
                    sendToMediaServiceWithAction(context, Constants.ACTION_FAVOURITE_TOGGLE_PLAYBACK, song = song)
                }
                Constants.ACTION_FAVOURITE_MUSIC_CLICKED -> sendToMediaServiceWithAction(context,
                    Constants.ACTION_FAVOURITE_MUSIC_CLICKED, song = song)

                Constants.ACTION_FAVOURITE_PLAY_NEXT_NOTIFICATION -> {
                    sendBroadcastToMainActivityWithAction(context,
                        Constants.ACTION_FAVOURITE_PLAY_NEXT_NOTIFICATION
                    )
                }
                Constants.ACTION_FAVOURITE_TOGGLE_PLAY_NOTIFICATION -> {
                    sendBroadcastToMainActivityWithAction(context,
                        Constants.ACTION_FAVOURITE_TOGGLE_PLAY_NOTIFICATION
                    )
                }
                Constants.ACTION_FAVOURITE_PLAY_PREVIOUS_NOTIFICATION -> {
                    sendBroadcastToMainActivityWithAction(context,
                        Constants.ACTION_FAVOURITE_PLAY_PREVIOUS_NOTIFICATION
                    )
                }
            }


        }
    }

}
