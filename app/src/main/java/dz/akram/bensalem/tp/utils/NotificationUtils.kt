package dz.akram.bensalem.tp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dz.akram.bensalem.tp.utils.Constants.ACTION_PAUSE
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY
import dz.akram.bensalem.tp.utils.Constants.CHANNEL_ID
import dz.akram.bensalem.tp.activities.MainActivity
import dz.akram.bensalem.tp.service.MediaService
import dz.akram.bensalem.tp.R
import dz.akram.bensalem.tp.activities.FavouriteActivity
import dz.akram.bensalem.tp.service.FavouriteNotificationReceiver
import dz.akram.bensalem.tp.service.NotificationReceiver
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_PLAY_NEXT_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_PLAY_PREVIOUS_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_FAVOURITE_TOGGLE_PLAY_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY_NEXT_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_PLAY_PREVIOUS_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_SKIP_NEXT
import dz.akram.bensalem.tp.utils.Constants.ACTION_SKIP_PREVIOUS
import dz.akram.bensalem.tp.utils.Constants.ACTION_TOGGLE_PLAY_NOTIFICATION
import dz.akram.bensalem.tp.utils.Constants.ACTION_UI_SKIP_NEXT
import dz.akram.bensalem.tp.utils.Constants.ACTION_UI_SKIP_PREVIOUS

object NotificationUtils {

    // create notification channel
   private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // create a notification
    fun createNotification(
        activity: MediaService,
        song: String,
        isPlaying: Boolean,
        isFromMainActivity : Boolean
    ): NotificationCompat.Builder {

        createNotificationChannel(activity)
        // Create an explicit intent for an Activity in your app
        val intent = Intent(activity,if (isFromMainActivity) MainActivity::class.java else FavouriteActivity::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)



        // prevPendingIntent
        val prevPendingIntent : PendingIntent = PendingIntent.getBroadcast(
            activity, 0,
            Intent(
                if (isFromMainActivity)  ACTION_PLAY_PREVIOUS_NOTIFICATION else ACTION_FAVOURITE_PLAY_PREVIOUS_NOTIFICATION,
                null,
                activity,
                if (isFromMainActivity) NotificationReceiver::class.java else FavouriteNotificationReceiver::class.java
            ), PendingIntent.FLAG_IMMUTABLE
        )

        // pausePendingIntent
        val pausePendingIntent : PendingIntent = PendingIntent.getBroadcast(
            activity,
            0,
            Intent(
               if (isFromMainActivity)  ACTION_TOGGLE_PLAY_NOTIFICATION else ACTION_FAVOURITE_TOGGLE_PLAY_NOTIFICATION,
                null,
                activity,
                if (isFromMainActivity) NotificationReceiver::class.java else FavouriteNotificationReceiver::class.java
            ),
            PendingIntent.FLAG_IMMUTABLE
        )

        // nextPendingIntent
        val nextPendingIntent : PendingIntent = PendingIntent.getBroadcast(
            activity,
            0,
            Intent(
                if (isFromMainActivity)   ACTION_PLAY_NEXT_NOTIFICATION else ACTION_FAVOURITE_PLAY_NEXT_NOTIFICATION,
                null,
                activity,
                if (isFromMainActivity) NotificationReceiver::class.java else FavouriteNotificationReceiver::class.java
            ),
            PendingIntent.FLAG_IMMUTABLE
        )


        return NotificationCompat.Builder(activity, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_play_circle_outline_24)
            .setContentTitle("Lecteur en cours")
            .setContentText(song)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

            // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            // Add media control buttons that invoke intents in your media service
            .addAction(R.drawable.ic_baseline_skip_previous_24, "Previous", prevPendingIntent) // #0
            .addAction(
                if (!isPlaying) R.drawable.ic_round_pause_circle_outline_24 else R.drawable.ic_baseline_play_circle_outline_24,
                if (!isPlaying) "Pause" else "Play",
                pausePendingIntent) // #1
            .addAction(R.drawable.ic_round_skip_next_24, "Next", nextPendingIntent) // #2
            .setOngoing(true)
    }

}
