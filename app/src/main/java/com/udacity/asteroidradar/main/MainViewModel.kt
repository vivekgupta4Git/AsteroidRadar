package com.udacity.asteroidradar.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Network.AsteroidApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel : ViewModel() {

    //Encapsulated status Live Data variable
    private var _status = MutableLiveData<String>()
    val status : LiveData<String>
    get() = _status

    init {
        getResponse()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getResponse(){

        //enqueue retrofit service
            AsteroidApi.retrofitService.getAsteroids(getTodayDate(),Constants.apikey).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _status.value = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _status.value = t.message
            }

        })

    }

  private  fun getTodayDate(): String {

        //getting instance of date
        val date = Calendar.getInstance().time
       //IDE suggested that Y pattern needs to first check for version
        val simpleDateFormatter = 
            //but small y is accepted by IDE
            SimpleDateFormat("yyyy-MM-dd")


        //returning Date as String
        return simpleDateFormatter.format(date)
    }

}