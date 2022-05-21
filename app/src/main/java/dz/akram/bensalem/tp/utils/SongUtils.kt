package dz.akram.bensalem.tp.utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import dz.akram.bensalem.tp.database.Song


object SongUtils {
    fun getAudioFiles(
        context: Context
    ) : List<Song>{

        val audioFiles = mutableListOf<Song>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        val columns = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.Media.ALBUM
        )

        // Display videos in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        Log.d("FavouriteActivityAkram", "Query start");
        val query  = context.contentResolver.query(
            collection,
            columns,
            null,
            null,
            sortOrder
        )
        if (query == null) {
            Log.d("FavouriteActivityAkram", "cursor is null");

        }

        query?.let{ cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val album = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)
            val path = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)

            while (cursor.moveToNext()){

                val id = cursor.getLong(idColumn)

                val song = Song(
                    id =  id,
                    artist = cursor.getString(artistColumn),
                    name = cursor.getString(displayNameColumn),
                    description = cursor.getString(durationColumn),
                    path = cursor.getString(path),
                    album = cursor.getString(album)
                )

                Log.d("FavouriteActivityAkram", "Song data added ${cursor.getString(path)}");
                audioFiles.add(song)
            }

        }

        Log.d("FavouriteActivityAkram", "Query end");
        return audioFiles
    }



}