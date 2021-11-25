package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.main.Filter
import com.udacity.asteroidradar.main.getDateAfterSevenDaysFromToday
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.asteroidradar.main.getTodayDate
import com.udacity.asteroidradar.network.asDatabaseModel
import org.json.JSONObject
import retrofit2.HttpException


class AsteroidsRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }


    fun getAsteroidBasedOnFilter(filter: Filter): LiveData<List<Asteroid>> {
        return when (filter) {
            Filter.SAVED -> {
                Transformations.map(database.asteroidDao.getAsteroids()) {
                    it.asDomainModel()
                }
            }
            Filter.SHOW_TODAY -> {
                Transformations.map(database.asteroidDao.getTodayAsteroid()) {
                    it.asDomainModel()
                }

            }
            else -> {
                Transformations.map(database.asteroidDao.getWeeklyAsteroids()) {
                    it.asDomainModel()
                }
            }

        }
    }


    suspend fun refreshAsteroids() {
try {

    withContext(Dispatchers.IO) {
        try {
            val responseString = AsteroidApi.retrofitService.getAsteroids(getTodayDate(),
                getDateAfterSevenDaysFromToday(), Constants.apikey)
            //parsing response String
            val list = parseAsteroidsJsonResult(JSONObject(responseString))
            //Network object to database object
            database.asteroidDao.insertAll(*list.asDatabaseModel())
        } catch (E: HttpException) {
            Log.e("Asteroid", "inside of RefreshAsteroid Method->" + E.message())
        }
    }

}catch (e : Exception){

}


    }
}