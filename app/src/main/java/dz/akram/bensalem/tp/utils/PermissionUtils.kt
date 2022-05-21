package dz.akram.bensalem.tp.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dz.akram.bensalem.tp.activities.MainActivity.Companion.MEDIA_LOCATION_PERMISSION_REQUEST_CODE
import dz.akram.bensalem.tp.activities.MainActivity.Companion.PERMISSION_READ_EXTERNAL_STORAGE

object PermissionUtils {

    //Check if you already have read storage permission
    private fun checkPermissionForRead(context: Context): Boolean {
        val result: Int = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        return result == PackageManager.PERMISSION_GRANTED
    }

    //Request Permission For Read Storage
    private fun requestPermissionForReadWrite(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), PERMISSION_READ_EXTERNAL_STORAGE
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestPermissionForAccessMediaLocation(context: Context) {
        Log.i("MainActivityAkram", "requestPermissionForAML")

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION),
            MEDIA_LOCATION_PERMISSION_REQUEST_CODE
        )

    }


    //Check if Permission granted for Accessing Media Location
    private fun isPermissionGrantedForMediaLocationAccess(context: Context): Boolean {
        Log.i("Tag", "checkPermissionForAML")
        val result: Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_MEDIA_LOCATION
                )
            } else {
                PackageManager.PERMISSION_GRANTED
            }
        return result == PackageManager.PERMISSION_GRANTED
    }


    fun checkPermissions(
        context: Context
    ) {         // check if the app has the permission to read the external storage
        if (!checkPermissionForRead(context)) {
            requestPermissionForReadWrite(context)
        } else if (!isPermissionGrantedForMediaLocationAccess(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissionForAccessMediaLocation(context)
            }
        }
    }


}