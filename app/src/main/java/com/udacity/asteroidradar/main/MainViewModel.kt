package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.AsteroidFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
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
/*
We are not using this as we are separating this logic out of view model
    //Encapsulated List of Asteroid
    private var _asteroidList = MutableLiveData<List<Asteroid>?>()
    val asteroidList : LiveData<List<Asteroid>?>
    get() = _asteroidList
*/

    /*
    creating repo
     */
   private val database = AsteroidDatabase.getInstance(application)
    private val repo = AsteroidsRepository(database)


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
                    repo.getAsteroidBasedOnFilter(AsteroidFilter.SHOW_SAVED)
                  //  repo.refreshAsteroids()
                    _status.value = Asteroid_Status.DONE

                }catch (e : Exception)
                {
                    _status.value = Asteroid_Status.ERROR
                }


        }
    }

    /*
    As repo is created, use repo to get list of asteroids.
    Same variable name as used previously, so we don't need to change in res folder
     */
    val asteroidList = repo.asteroids

/*
Finally using Moshi to get picture of the day
 */
    private suspend fun getPictureOfDay(){
        val    responseObject =  AsteroidApi.retrofitService.getPicOfTheDay(Constants.apikey)
        if(responseObject?.mediaType=="image")
            _picOfTheDay.value = responseObject

    }


    fun updateFilter(filter : AsteroidFilter){
        Log.i("Asteriods","Recieved filter value =$filter")
        repo.getAsteroidBasedOnFilter(filter)
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

@SuppressLint("NewApi")
fun getDateAfterSevenDaysFromToday(): String{


    //getting instance of date
    val date = Calendar.getInstance().time
    val cal = Calendar.getInstance()

    val simpleDateFormatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT,Locale.getDefault())
    cal.time = simpleDateFormatter.parse(date.toString())
    cal.add(Calendar.DATE,7)
        return simpleDateFormatter.format(cal.time)
}