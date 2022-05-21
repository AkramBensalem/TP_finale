package dz.akram.bensalem.tp.repositories

import android.content.Context
import dz.akram.bensalem.tp.database.Song
import dz.akram.bensalem.tp.utils.SongUtils

class LocalStorageRepository(
    val context : Context
) {

    suspend fun getAllSong() : List<Song> {
        return SongUtils.getAudioFiles(context)
    }
}