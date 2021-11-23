package com.udacity.asteroidradar

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidEntity
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/*
@RunWith(AndroidJUnit4::class)
class AsteroidDatabaseTest {
    private lateinit var asteroidDao: AsteroidDao
    private lateinit var database: AsteroidDatabase

    @Before
    fun createDb(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        database = Room.inMemoryDatabaseBuilder(appContext,AsteroidDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        asteroidDao = database.asteroidDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        database.close()
    }

    @Test
    @Throws(Exception::class)
     fun insertAndGetAsteroid(){
        val asteroid_one = AsteroidEntity(1,"abc","2021-11-11",2.5,222.00,234.2,222.0,false)
        asteroidDao.insert(asteroid_one)
        val list = asteroidDao.get()
        assertEquals(list?.size,1)
    }

}*/
