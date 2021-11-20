package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parsePicOfTheDay
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

enum class Asteroid_Status{
    LOADING,
    DONE,
    ERROR
}

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel : ViewModel() {

    //Encapsulated status Live Data variable
    private var _status = MutableLiveData<Asteroid_Status>()
    val status : LiveData<Asteroid_Status>
    get() = _status


    //Encapsulated pic of the day variable
    private var _picOfTheDay = MutableLiveData<String>()
    val pictureOfDay : LiveData<String>
    get() = _picOfTheDay

    //Encapsulated List of Asteroid
    private var _asteroidList = MutableLiveData<List<Asteroid>?>()
    val asteroidList : LiveData<List<Asteroid>?>
    get() = _asteroidList

    init {
        getResponse()
    }

    /*
I was getting error while parsing json response saying "expected begin_array but was begin_object"

So, I used  provided parsing method but it also has bug
While searching for answer in udacity help center, I came across
https://knowledge.udacity.com/questions/720081 which gave my answer so using it.

 */

    private fun getResponse(){
        _status.value = Asteroid_Status.LOADING
//using coroutines
        viewModelScope.launch {

                try {
                    //using retrofit service
                    val asteroidResult: String =
                        AsteroidApi.retrofitService.getAsteroids(getTodayDate(),Constants.apikey)
                    //Getting parsed asteroid List
                    _asteroidList.value = parseAsteroidsJsonResult(JSONObject(asteroidResult))
                    Log.i("Asteroid","in View Model, parsed List " + _asteroidList.value.toString())
                    _status.value = Asteroid_Status.DONE


                    //as status is ok, we will retrieve pic of the day
              val string =  AsteroidApi.retrofitService.getPicOfTheDay(Constants.apikey)
                    val pictureOfDay = parsePicOfTheDay(JSONObject(string))
                    if(pictureOfDay?.mediaType=="image")
                _picOfTheDay.value = pictureOfDay.url


                }catch (e : Exception)
                {
                    _asteroidList.value = ArrayList()
                    _status.value = Asteroid_Status.ERROR
                }


        }
    }


    @SuppressLint("NewApi")
    private  fun getTodayDate(): String {

        //getting instance of date
        val date = Calendar.getInstance().time
       //IDE suggested that Y pattern needs to first check for version
        val simpleDateFormatter = 
            //but small y is accepted by IDE
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

        //returning Date as String
        return simpleDateFormatter.format(date)
    }

}