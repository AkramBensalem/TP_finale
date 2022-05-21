package dz.akram.bensalem.tp.viewmodels.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dz.akram.bensalem.tp.database.Song
import dz.akram.bensalem.tp.repositories.FavouriteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val repository : FavouriteRepository
) : ViewModel() {


    private val _currentSongPosition = MutableStateFlow<Int?>(null)
    val currentSongPosition: StateFlow<Int?> = _currentSongPosition


    private val _favouriteList = MutableStateFlow<List<Song>>(
        value = listOf()
    )

    val favouriteList: StateFlow<List<Song>>
        get() = _favouriteList


    init {
        getAllFavouriteSong()
    }

    private fun getAllFavouriteSong() {
        viewModelScope.launch {
            repository.getFavouriteSongs().collect {
                _favouriteList.value = it
            }
        }
    }

    fun addNewSong(song: Song) { //add layer of protection
        viewModelScope.launch { // launch a new coroutine in viewModelScope to not bloc UI
            repository.addNewSong(song)
            }
        }

    fun deleteSong(song: Song) {
        viewModelScope.launch {
            repository.deleteSong(song)
        }
    }




    fun currentSong() : Song? {
        if (favouriteList.value.isEmpty()) return null
        return _favouriteList.value[_currentSongPosition.value ?: 0]
    }

    fun nextSong() : Song?{
        val nextSongIndex = _currentSongPosition.value?.plus(1)
        if (nextSongIndex != null && nextSongIndex < favouriteList.value.size) {
            _currentSongPosition.value = nextSongIndex
            return _favouriteList.value[nextSongIndex]
        }
        return null
    }

    fun previousSong() : Song?{
        val previousSongIndex = _currentSongPosition.value?.minus(1)
        if (previousSongIndex != null && previousSongIndex >= 0) {
            _currentSongPosition.value = previousSongIndex
            return _favouriteList.value[previousSongIndex]
        }
        return null
    }

    fun updateCurrentSongPosition(position: Int) {
        _currentSongPosition.value = position
    }

    fun getSongByPosition(position: Int) : Song? {
        if (position >= 0 && position < favouriteList.value.size) {
            return _favouriteList.value[position]
        }
        return null
    }


}