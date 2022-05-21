package dz.akram.bensalem.tp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dz.akram.bensalem.tp.utils.Constants.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class SongDatabase : RoomDatabase() {
    abstract fun favouriteSongDao(): FavouriteDao
    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): SongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}




