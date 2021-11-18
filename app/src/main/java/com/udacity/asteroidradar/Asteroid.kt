package com.udacity.asteroidradar

import android.os.Parcelable
import com.squareup.moshi.Json
//import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parcelize


@Parcelize
data class Asteroid(val id: Long,
                    @Json(name="neo_reference_id")
                    val codename: String,
                    @Json(name ="close_approach_date") val closeApproachDate: String,
                    @Json(name="absolute_magnitude_h")val absoluteMagnitude: Double,
                    @Json(name="estimated_diameter")val estimatedDiameter: Double,
                    @Json (name="relative_velocity")val relativeVelocity: Double,
                    @Json(name="miss_distance")val distanceFromEarth: Double,
                    @Json (name="is_potentially_hazardous_asteroid")
                    val isPotentiallyHazardous: Boolean) : Parcelable

