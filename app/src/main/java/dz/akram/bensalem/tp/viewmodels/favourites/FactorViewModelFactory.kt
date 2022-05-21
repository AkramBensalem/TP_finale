package dz.akram.bensalem.tp.viewmodels.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dz.akram.bensalem.tp.repositories.FavouriteRepository


class FactorViewModelFactory(private val repository: FavouriteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouriteViewModel(repository) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }
}

