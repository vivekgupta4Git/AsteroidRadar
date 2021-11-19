package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.os.Build
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

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel : ViewModel() {

    //Encapsulated status Live Data variable
    private var _status = MutableLiveData<String>()
    val status : LiveData<String>
    get() = _status


    //Encapsulated pic of the day variable
    private var _picOfTheDay = MutableLiveData<String>()
    val pictureOfDay : LiveData<String>
    get() = _picOfTheDay

    init {
        getResponse()
    }

    private fun getResponse(){
    /*
    I was getting error while parsing json response saying "expected begin_array but was begin_object"

    So, I used  provided parsing method but it also has bug
    While searching for answer in udacity help center, I came across
    https://knowledge.udacity.com/questions/720081 which gave my answer so using it.

     */

/*
using coroutines
 */
        viewModelScope.launch {

                try {

                    val asteroidResult: String =
                        AsteroidApi.retrofitService.getAsteroids(getTodayDate(),Constants.apikey)
                    _status.value = parseAsteroidsJsonResult(JSONObject(asteroidResult)).size.toString()

                    //as status is ok, we will retrieve pic of the day
              val string =  AsteroidApi.retrofitService.getPicOfTheDay(Constants.apikey)
                _picOfTheDay.value = parsePicOfTheDay(JSONObject(string)).toString()

                }catch (e : Exception)
                {
                    _status.value = "Failure" +e.message
                }


        }
 /*       //enqueue retrofit service
            AsteroidApi.retrofitService.getAsteroids(getTodayDate(),Constants.apikey).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                        val jobject = response.body()?.let { JSONObject(it) }
                        _status.value = jobject?.let { parseAsteroidsJsonResult(it).size.toString() }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _status.value = t.message
            }

        })
*/
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