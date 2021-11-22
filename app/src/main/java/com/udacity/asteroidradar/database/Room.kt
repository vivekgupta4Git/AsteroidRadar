package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Query("select * from databaseasteroid")
    fun getAsteroid() : LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class],version = 1)
abstract class AsteroidRoomDatabase : RoomDatabase(){
    abstract val asteroidDao : AsteroidDao

}

private lateinit var INSTANCE: AsteroidRoomDatabase

public fun getDatabase(context: Context): AsteroidRoomDatabase{
    synchronized(AsteroidRoomDatabase::class.java){
        if(!::INSTANCE.isInitialized)
        {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AsteroidRoomDatabase::class.java,"Asteroids").build()

        }
    }
    return INSTANCE
}