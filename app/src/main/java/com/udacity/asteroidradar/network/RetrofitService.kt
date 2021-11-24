package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query




private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL)
        .build()

    interface AsteroidApiService {
        @GET("neo/rest/v1/feed")
     suspend  fun  getAsteroids( @Query("START_DATE") start: String,
                                @Query("END_DATE") end :String,
                           @Query("API_KEY") key:String
                           ) : String

        @GET("planetary/apod")
        suspend fun getPicOfTheDay(@Query("api_key") key:String) : PictureOfDay

    }
    object AsteroidApi{
        val retrofitService : AsteroidApiService by lazy {
            retrofit.create(AsteroidApiService::class.java)
        }
    }
