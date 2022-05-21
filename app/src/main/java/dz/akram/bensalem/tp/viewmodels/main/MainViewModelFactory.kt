package dz.akram.bensalem.tp.viewmodels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dz.akram.bensalem.tp.repositories.FavouriteRepository
import dz.akram.bensalem.tp.repositories.LocalStorageRepository


class MainViewModelFactory(
    private val localStorageRepository: LocalStorageRepository,
    private val favouriteRepository: FavouriteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(localStorageRepository, favouriteRepository) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }
}