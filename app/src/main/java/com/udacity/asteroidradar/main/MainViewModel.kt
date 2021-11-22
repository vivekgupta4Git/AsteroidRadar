package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parsePicOfTheDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
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
class MainViewModel(application: Application) : AndroidViewModel(application) {

    //Encapsulated status Live Data variable
    private var _status = MutableLiveData<Asteroid_Status>()
    val status : LiveData<Asteroid_Status>
    get() = _status


    //Encapsulated pic of the day variable
    private var _picOfTheDay = MutableLiveData<String>()
    val pictureOfDay : LiveData<String>
    get() = _picOfTheDay


    //Encapsulated navigation
    private var _navigateToDetailFragment  = MutableLiveData<Asteroid?>()
    val navigateToDetailFrgament : MutableLiveData<Asteroid?>
    get() = _navigateToDetailFragment


private val database = getDatabase(application)
private val repository = AsteroidsRepository(database)


    init {getResponse()}

    fun displayDetailFragment(asteroid: Asteroid){
        _navigateToDetailFragment.value = asteroid
    }

    fun displayDetailFragmentComplete()
    {
        _navigateToDetailFragment.value  = null
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
                try {
                  repository.refreshAsteroidsList()
                    _status.value = Asteroid_Status.DONE
                    var responseString:String?=null
                    //as status is ok, we will retrieve pic of the day
                  responseString =  AsteroidApi.retrofitService.getPicOfTheDay(Constants.apikey)
                    //getting parsed picture of the day
                    val pictureOfDay = parsePicOfTheDay(JSONObject(responseString))
                    //Show picture only if it's media type is image
                    if(pictureOfDay?.mediaType=="image")
                _picOfTheDay.value = pictureOfDay.url
                }catch (e : Exception)
                {
                    _status.value = Asteroid_Status.ERROR
                }

        }
    }

    val asteroid = repository.asteroids


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AndroidViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AndroidViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")

        }
    }


}

@SuppressLint("NewApi")
public  fun getTodayDate(): String {

    //getting instance of date
    val date = Calendar.getInstance().time
    //IDE suggested that Y pattern needs to first check for version
    val simpleDateFormatter =
        //but small y is accepted by IDE
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    //returning Date as String
    return simpleDateFormatter.format(date)
}
