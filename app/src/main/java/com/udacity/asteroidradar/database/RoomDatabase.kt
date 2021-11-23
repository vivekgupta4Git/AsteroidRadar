package com.udacity.asteroidradar.database

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.parcelize.Parcelize


@Dao
interface AsteroidDao{

  //this can be used to show menu item for saved
  //get all asteroids from database sorted by date.
@Query("Select * from asteroid_table Order by closeApproachDate")
fun getAsteroids() :LiveData< List<AsteroidEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertAll(vararg asteroids : AsteroidEntity)

  //get asteroids for menu item weekly
  @Query("SELECT * FROM asteroid_table WHERE date(closeApproachDate) BETWEEN date('now') AND date('now','+7 days') ORDER BY date(closeApproachDate) ASC")
  fun getWeeklyAsteroids(): LiveData<List<AsteroidEntity>>


  //For menu item , Today
  @Query("SELECT * FROM asteroid_table WHERE date(closeApproachDate)=date('now')")
  fun getTodayAsteroid(): LiveData<List<AsteroidEntity>>


  @Query("DELETE FROM asteroid_table ")
  fun clear()
}

@Database(entities = [AsteroidEntity::class],version = 1,exportSchema = false)
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
