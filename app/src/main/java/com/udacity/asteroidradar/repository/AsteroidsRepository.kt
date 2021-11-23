package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseAsteroidsJsonResultToDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.main.getDateAfterSevenDaysFromToday
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.asteroidradar.main.getTodayDate
import com.udacity.asteroidradar.network.asDatabaseModel
import org.json.JSONObject


class AsteroidsRepository(private val database: AsteroidDatabase) {

val asteroids : LiveData<List<Asteroid>> =
    Transformations.map(database.asteroidDao.getAsteroids()){
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val responseString =
                AsteroidApi.retrofitService.getAsteroids(getTodayDate(),
                    getDateAfterSevenDaysFromToday() ,Constants.apikey)
        /*   val list = parseAsteroidsJsonResultToDatabaseModel(JSONObject(responseString))
            database.asteroidDao.insertAll(*list.toTypedArray())
*/
//Network object to database object
val list = parseAsteroidsJsonResult(JSONObject(responseString))
database.asteroidDao.insertAll(*list.asDatabaseModel())
        }
    }


}