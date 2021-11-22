package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidRoomDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.asteroidradar.main.getTodayDate
import com.udacity.asteroidradar.network.NetworkAsteroid
import com.udacity.asteroidradar.network.NetworkAsteroidContainer
import kotlinx.coroutines.Deferred
import org.json.JSONObject


class AsteroidsRepository(private val database : AsteroidRoomDatabase)
{

    val asteroids : LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroid()){
            it.asDomainModel()
        }

    suspend fun refreshAsteroidsList(){
        withContext(Dispatchers.IO){
            //using retrofit service
            val asteroidResult: String =
                AsteroidApi.retrofitService.getAsteroids(getTodayDate(),Constants.apikey).await()
            //Getting parsed asteroid List
            val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResult))
            database.asteroidDao.insertAll(*asteroidList.toTypedArray())

        }
    }
}