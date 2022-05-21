package dz.akram.bensalem.tp.repositories

import dz.akram.bensalem.tp.database.FavouriteDao
import dz.akram.bensalem.tp.database.Song

class FavouriteRepository (
    private val favouriteDao: FavouriteDao
    ) {

    fun getFavouriteSongs() = favouriteDao.getFavouriteSongs()

    suspend fun addNewSong(song: Song) {
        return favouriteDao.insertSong(song)
    }

    suspend fun deleteSong(song: Song) {
        return favouriteDao.delete(song)
    }

}