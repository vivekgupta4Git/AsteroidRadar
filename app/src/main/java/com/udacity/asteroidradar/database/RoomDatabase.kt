package com.udacity.asteroidradar.database

import android.content.Context
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "asteroid_table")
data class AsteroidEntity(
                        @PrimaryKey
                          val id: Long,
                          val codename: String,
                          val closeApproachDate: String,
                          val absoluteMagnitude: Double,
                          val estimatedDiameter: Double,
                          val relativeVelocity: Double,
                          val distanceFromEarth: Double,
                          val isPotentiallyHazardous: Boolean) : Parcelable

fun List<AsteroidEntity>.asDomainModel() : List<Asteroid>{
  return map {
    Asteroid(
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

@Dao
interface AsteroidDao{
@Query("Select * from asteroid_table Order by closeApproachDate")
 fun get() : LiveData<List<AsteroidEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend  fun insert(vararg asteroid : AsteroidEntity)


}

@Database(entities = [AsteroidEntity::class],version = 2,exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase(){

  abstract val asteroidDao : AsteroidDao

  companion object{

    @Volatile
    private var INSTANCE : AsteroidDatabase? = null

    fun getInstance(context: Context) : AsteroidDatabase{
    synchronized(this)
    {
      var instance = INSTANCE

      if(instance == null)
      {
        instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                  "asteroid_history_database")
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
      }


      return instance
    }


    }
  }
}
