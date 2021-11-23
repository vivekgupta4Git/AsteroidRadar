package com.udacity.asteroidradar.repository

import android.webkit.JsPromptResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResultToDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.asteroidradar.main.getTodayDate
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) {

val asteroids : LiveData<List<Asteroid>> =
    Transformations.map(database.asteroidDao.getAsteroids()){
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val responseString =
                AsteroidApi.retrofitService.getAsteroids(getTodayDate(), Constants.apikey)
            val list = parseAsteroidsJsonResultToDatabaseModel(JSONObject(responseString))

            database.asteroidDao.insertAll(*list.toTypedArray())

        }
    }


}