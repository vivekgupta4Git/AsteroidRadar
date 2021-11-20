package com.udacity.asteroidradar.api

import android.annotation.SuppressLint
import android.net.Uri
import androidx.core.net.toUri
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



/*
parse method for pic of the day
 */
fun parsePicOfTheDay(jsonObject : JSONObject): PictureOfDay? {
    val keys = jsonObject.keys()
    var urlString :String? =null
    var mediaType: String? = null
    var title : String?=null
    keys.forEach {
            if(it=="media_type")
            mediaType = jsonObject.getString(it)

            if(it=="url")
            urlString = jsonObject.getString(it)

            if(it=="title")
            title = jsonObject.getString(it)
                }

    if(mediaType!=null && urlString!=null && title!=null)
        return PictureOfDay(mediaType!!, title!!, urlString!!)
    else
        return PictureOfDay("","","")
}

/*
New parse method got from https://knowledge.udacity.com/questions/720081
 */

fun parseAsteroidsJsonResult(jsonObject: JSONObject): List<Asteroid> {
    val asteroidList = mutableListOf<Asteroid>()
    val nearEarthObjectsJson = jsonObject.getJSONObject("near_earth_objects")
    val dateList: MutableIterator<String> = nearEarthObjectsJson.keys()


    val dateListSorted = dateList.asSequence().sorted()

        dateListSorted.forEach {
            val key: String = it
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(key)
            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")
                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")
                val asteroid = Asteroid(
                    id,
                    codename,
                    key,
                    absoluteMagnitude,
                    estimatedDiameter,
                    relativeVelocity,
                    distanceFromEarth,
                    isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }


    return asteroidList
}


//the provided parse method has bug
fun OldparseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {

        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        if (dateAsteroidJsonArray != null) {
            for (i in 0 until dateAsteroidJsonArray.length()) {

                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)

                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter").getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson.getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity").getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance").getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson.getBoolean("is_potentially_hazardous_asteroid")


                val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
                    estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)

                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}

@SuppressLint("NewApi")
private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}