package com.udacity.asteroidradar.database

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.parcelize.Parcelize


@Dao
interface AsteroidDao{
@Query("Select * from asteroid_table Order by closeApproachDate")
fun get() : List<AsteroidEntity>?

  @Insert
   fun insert(asteroid : AsteroidEntity)

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
