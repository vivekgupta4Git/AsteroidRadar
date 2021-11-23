package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.worker.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication :Application() {


    val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    /*
    As per project requirement,
    The app downloads the next 7 days asteroids and saves them in the database
    once a day using workManager with requirements of internet connection and device plugged in.
    The app can display saved asteroids from the database even if internet connection
     is not available

     */

    val constaints = Constraints.Builder()
        .setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    private fun setupRecurringWork() {

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1,TimeUnit.DAYS)
            .setConstraints(constaints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("worker1",
            ExistingPeriodicWorkPolicy.KEEP,repeatingRequest)

    }
}