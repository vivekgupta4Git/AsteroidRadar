package com.udacity.asteroidradar

import android.os.Parcelable
import com.squareup.moshi.Json
//import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parcelize

/*
Domain Objects

 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *

 */


/**
 * Asteroid having properties like id , codename
 */

@Parcelize
data class Asteroid(val id: Long,
                    val codename: String,
                     val closeApproachDate: String,
                    val absoluteMagnitude: Double,
                    val estimatedDiameter: Double,
                    val relativeVelocity: Double,
                    val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable

