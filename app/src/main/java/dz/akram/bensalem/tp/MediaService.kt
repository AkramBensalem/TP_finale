package dz.akram.bensalem.tp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import dz.akram.bensalem.tp.NotificationUtils.ACTION_STOP

class MediaService : Service() {

    // binder given to clients
    val binder = MyBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class MyBinder : Binder(){
        fun currentService(): MediaService {
            return this@MediaService
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // handle command
        intent?.let {
            if (intent.action == ACTION_STOP) {
                stopForeground(true)
                stopSelf()
            }else showNotification(song = intent.action ?: "")
        }

        return START_STICKY
    }



    // show notification
    fun showNotification(
        song : String
    ) {
        val notification = NotificationUtils.createNotification(
            activity = this,
            song= song
        ).build()
        startForeground(1, notification)
    }
}