package dz.akram.bensalem.tp.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dz.akram.bensalem.tp.database.Song
import dz.akram.bensalem.tp.repositories.FavouriteRepository
import dz.akram.bensalem.tp.repositories.LocalStorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val localStorageRepository: LocalStorageRepository,
    private val favouriteRepository: FavouriteRepository
) : ViewModel()   {


    private val _currentSongPosition = MutableStateFlow<Int?>(null)
    val currentSongPosition: StateFlow<Int?> = _currentSongPosition

    private val _isFetchAllData = MutableStateFlow(false)
    val isFetchAllData: StateFlow<Boolean> = _isFetchAllData

    private val _songList = MutableStateFlow<List<Song>>(
        value = listOf()
    )

    val songList: StateFlow<List<Song>>
        get() = _songList


    init {
        getAllSongListFromStotage()
    }

    private fun getAllSongListFromStotage() {
        viewModelScope.launch {
            _songList.value = localStorageRepository.getAllSong()
            _isFetchAllData.value = true
        }
    }

    fun addToFavourite(currentSong: Song) {
        viewModelScope.launch {
            favouriteRepository.addNewSong(currentSong)
        }
    }

    fun currentSong() : Song? {
        if (_songList.value.isEmpty()) return null
        return _songList.value[_currentSongPosition.value ?: 0]
    }

    fun nextSong() : Song?{
        val nextSongIndex = _currentSongPosition.value?.plus(1)
        if (nextSongIndex != null && nextSongIndex < _songList.value.size) {
            _currentSongPosition.value = nextSongIndex
            return _songList.value[nextSongIndex]
        }
        return null
    }

    fun previousSong() : Song?{
        val previousSongIndex = _currentSongPosition.value?.minus(1)
        if (previousSongIndex != null && previousSongIndex >= 0) {
            _currentSongPosition.value = previousSongIndex
            return _songList.value[previousSongIndex]
        }
        return null
    }

    fun updateCurrentSongPosition(position: Int) {
        _currentSongPosition.value = position
    }

    fun getSongByPosition(position: Int) : Song? {
        if (position >= 0 && position < _songList.value.size) {
            return _songList.value[position]
        }
        return null
    }

}