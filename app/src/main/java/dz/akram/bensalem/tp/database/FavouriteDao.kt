package dz.akram.bensalem.tp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import dz.akram.bensalem.tp.utils.Constants.SONG_TABLE_NAME

@Dao
interface FavouriteDao {

    @Query("SELECT * FROM $SONG_TABLE_NAME")
    fun getFavouriteSongs(): Flow<List<Song>>

    @Query("SELECT * FROM $SONG_TABLE_NAME S WHERE S.id LIKE :id LIMIT 1")
    fun findByIde(id: Int): Flow<Song>

    @Query("SELECT * FROM $SONG_TABLE_NAME S WHERE S.name LIKE :name")
    fun findByName(name: String): Flow<Song>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)

    @Delete
    suspend fun delete(song: Song)
}
