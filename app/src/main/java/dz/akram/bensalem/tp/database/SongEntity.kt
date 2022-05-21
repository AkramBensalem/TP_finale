package dz.akram.bensalem.tp.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import dz.akram.bensalem.tp.utils.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.SONG_TABLE_NAME)
@Parcelize
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val path: String,
    val artist: String? = null,
    val description: String?= null,
    val album: String? = null
): Parcelable
