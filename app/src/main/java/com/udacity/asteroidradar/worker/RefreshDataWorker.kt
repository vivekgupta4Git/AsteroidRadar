package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWorker(context: Context, params:WorkerParameters) : CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {

        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidsRepository(database)
        return try {

            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}