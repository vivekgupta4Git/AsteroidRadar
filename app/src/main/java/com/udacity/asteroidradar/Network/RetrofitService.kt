package com.udacity.asteroidradar.Network

import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

    interface AsteroidApiService {
        @GET("neo/rest/v1/feed")
        fun  getAsteroids( @Query("START_DATE") start: String,
                           @Query("API_KEY") key:String
                           ) : Call<String>

    }
    object AsteroidApi{
        val retrofitService : AsteroidApiService by lazy {
            retrofit.create(AsteroidApiService::class.java)
        }
    }
