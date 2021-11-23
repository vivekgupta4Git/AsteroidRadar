package com.udacity.asteroidradar.Repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroid: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.get()) {
        it.asDomainModel()
    }


    suspend fun refreshAsteroidList(){
        val responseString = AsteroidApi.retrofitService.getAsteroids(getTodayDate(),Constants.apikey)
       val asteroidList =  parseAsteroidsJsonResult(JSONObject(responseString))
        Log.i("Asteroid",asteroidList.toString())
       // database.asteroidDao.insert(*asteroidList.asDatabaseModel().toTypedArray())
       // database.asteroidDao.insert(asteroidList.asDatabaseModel())
    }
}




@SuppressLint("NewApi")
  fun getTodayDate(): String {

    //getting instance of date
    val date = Calendar.getInstance().time
    //IDE suggested that Y pattern needs to first check for version
    val simpleDateFormatter =
        //but small y is accepted by IDE
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    //returning Date as String
    return simpleDateFormatter.format(date)
}
