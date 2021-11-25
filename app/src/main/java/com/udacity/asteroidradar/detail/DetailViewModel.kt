package com.udacity.asteroidradar.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class DetailViewModel(asteroid: Asteroid) :ViewModel(){

    //Encapsulated selected Asteroid
    private var _selectedAsteroid = MutableLiveData<Asteroid>()
    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    init {
        _selectedAsteroid.value = asteroid
    }


}

/*
//We don't need application scope here..
class DetailViewModel(asteroid: Asteroid,application: Application) :ViewModel(){

    //Encapsulated selected Asteroid
    private var _selectedAsteroid = MutableLiveData<Asteroid>()
    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    init {
        _selectedAsteroid.value = asteroid
    }

}*/
