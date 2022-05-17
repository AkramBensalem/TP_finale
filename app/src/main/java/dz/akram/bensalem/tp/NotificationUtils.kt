package dz.akram.bensalem.tp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationUtils {

    private const val CHANNEL_ID = "music_id"


   const val  ACTION_STOP = "${BuildConfig.APPLICATION_ID}.stop"

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
    ): NotificationCompat.Builder {

        createNotificationChannel(activity)
        // Create an explicit intent for an Activity in your app
        val intent = Intent(activity, MainActivity::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // prevPendingIntent
        val prevPendingIntent : PendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // pausePendingIntent
        val pausePendingIntent : PendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // nextPendingIntent
        val nextPendingIntent : PendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)


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
            .addAction(R.drawable.ic_round_pause_circle_outline_24, "Pause", pausePendingIntent) // #1
            .addAction(R.drawable.ic_round_skip_next_24, "Next", nextPendingIntent) // #2


    }

}
