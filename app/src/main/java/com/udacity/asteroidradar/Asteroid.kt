package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.lifecycle.Transformations.map
import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.AsteroidEntity
//import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parcelize


@Parcelize
data class Asteroid(val id: Long,
                    val codename: String,
                     val closeApproachDate: String,
                    val absoluteMagnitude: Double,
                    val estimatedDiameter: Double,
                    val relativeVelocity: Double,
                    val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable


fun List<Asteroid>.asDatabaseModel() : List<AsteroidEntity>{
    return map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}
