package com.udacity.asteroidradar.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.Asteroid

class DetailViewModelFactory(
    private val asteroid: Asteroid,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java))
        {
            return DetailViewModel(asteroid) as T
        }
        throw java.lang.IllegalArgumentException("Unknown Viewmodel class")
    }
}
