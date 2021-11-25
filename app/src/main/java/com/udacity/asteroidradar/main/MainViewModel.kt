package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*


enum class Asteroid_Status{
    LOADING,
    DONE,
    ERROR
}
/*
Copied from knowledge center
 */
enum class MenuItemFilter(val value:String){
    SHOW_WEEK("week"),
    SHOW_TODAY("today"),
    SAVED("saved")
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

    //menu
    private val _menuItem = MutableLiveData<MenuItemFilter>()
    private val menuItem:LiveData<MenuItemFilter>
    get() = _menuItem



    //Encapsulated List of Asteroid
    private var _asteroidList = MutableLiveData<List<Asteroid>?>()
    val asteroidList : LiveData<List<Asteroid>?>
    get() = _asteroidList


    /*
    creating repo
     */
   private val database = AsteroidDatabase.getInstance(application)
    private val repo = AsteroidsRepository(database)


    //Encapsulated navigation
    private var _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFrgament : LiveData<Asteroid>
    get() = _navigateToDetailFragment


    /*
    copied from knowledege center to make recycler view update filter.
     */
    private val asteroidListObserver = androidx.lifecycle.Observer<List<Asteroid>>{
        //update new list to recycler view
        _asteroidList.value = it
    }

    private  var asteroidListLiveData : LiveData<List<Asteroid>>

    init {
        /*
      line->  THIS IS NOT MY CODE, USING SOLUTION FROM KNOWLEDGE CENTER
         */
   /* line 1*/     asteroidListLiveData = repo.getAsteroidBasedOnFilter(MenuItemFilter.SAVED)

   /*line 2*/     asteroidListLiveData.observeForever(asteroidListObserver)

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



private  fun getResponse() {
try {
    viewModelScope.launch {
        try {
            _status.value = Asteroid_Status.LOADING
            getPictureOfDay()
            repo.refreshAsteroids()
            repo.getAsteroidBasedOnFilter(MenuItemFilter.SAVED)

            _status.value = Asteroid_Status.DONE

        } catch (e: Exception) {
            Log.e("Asteroid", "getResponse Method->" + e.toString())
            _status.value = Asteroid_Status.ERROR
        }
    }

}catch (e : Exception)
{
    _status.value = Asteroid_Status.ERROR
}

}


/*
Finally using Moshi to get picture of the day
 */
    private suspend fun getPictureOfDay(){
    try {

        viewModelScope.launch {
            try {
                val    responseObject =  AsteroidApi.retrofitService.getPicOfTheDay(Constants.apikey)

                if(responseObject?.mediaType=="image")
                {
                    _picOfTheDay.value = responseObject
                }

            }   catch (e : Exception){
                Log.e("Asteroid","Inside of getPictureOfDay Method->" +e.toString())
            }
        }


    }catch (e : Exception){

    }


    }


    fun updateFilter(filter : MenuItemFilter){
        asteroidListLiveData = repo.getAsteroidBasedOnFilter(filter)
        asteroidListLiveData.observeForever(asteroidListObserver)
    }

    override fun onCleared() {
        super.onCleared()
        asteroidListLiveData.removeObserver(asteroidListObserver)
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
val calendar  = Calendar.getInstance()
    //was getting error in response from server when adding 7 days instead using 5 days
    calendar.add(Calendar.DATE,5)
    val date: Date = calendar.time
    val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
    val dateString = formatter.format(date)

    return dateString
}