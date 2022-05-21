package dz.akram.bensalem.tp

import android.app.Application
import dz.akram.bensalem.tp.database.SongDatabase
import dz.akram.bensalem.tp.repositories.FavouriteRepository
import dz.akram.bensalem.tp.repositories.LocalStorageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ApplicationSong : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { SongDatabase.getDatabase(this, applicationScope) }
    val favouriteRepository by lazy { FavouriteRepository(database.favouriteSongDao()) }
    val localStorageRepository by lazy { LocalStorageRepository(this) }
}