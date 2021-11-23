package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

enum class Asteroid_Status{
    LOADING,
    DONE,
    ERROR
}

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    //Encapsulated status Live Data variable
    private var _status = MutableLiveData<Asteroid_Status>()
    val status : LiveData<Asteroid_Status>
    get() = _status


    //Encapsulated pic of the day variable
    private var _picOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay : LiveData<PictureOfDay>
    get() = _picOfTheDay

    //Encapsulated List of Asteroid
    private var _asteroidList = MutableLiveData<List<Asteroid>?>()
    val asteroidList : LiveData<List<Asteroid>?>
    get() = _asteroidList

    //Encapsulated navigation
    private var _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFrgament : LiveData<Asteroid>
    get() = _navigateToDetailFragment



    init {
            getResponse()


    }

    fun displayDetailFragment(asteroid: Asteroid){
        _navigateToDetailFragment.value = asteroid
    }

    fun displayDetailFragmentComplete()
    {
        _navigateToDetailFragment.value = null
    }

    /*
I was getting error while parsing json response saying "expected begin_array but was begin_object"

So, I used  provided parsing method but it also has bug
While searching for answer in udacity help center, I came across
https://knowledge.udacity.com/questions/720081 which gave my answer so using it.

 */

private fun getResponse(){
        _status.value = Asteroid_Status.LOADING

        viewModelScope.launch {
        getPictureOfDay()
                try {
                    val asteroidResult: String =
                        AsteroidApi.retrofitService.getAsteroids(getTodayDate(),Constants.apikey)
                    _asteroidList.value = parseAsteroidsJsonResult(JSONObject(asteroidResult))
                    _status.value = Asteroid_Status.DONE

                }catch (e : Exception)
                {
                    //setting list to empty
                    _asteroidList.value = ArrayList()
                    _status.value = Asteroid_Status.ERROR
                }


        }
    }

/*
Finally using Moshi to get picture of the day
 */
    private suspend fun getPictureOfDay(){
        val    responseObject =  AsteroidApi.retrofitService.getPicOfTheDay(Constants.apikey)
        if(responseObject?.mediaType=="image")
            _picOfTheDay.value = responseObject

    }




    class Factory(val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java))
                return MainViewModel(application) as T
            throw
                    IllegalArgumentException("Unknown viewmodel")
        }

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


