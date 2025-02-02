package com.example.ominitracker.schedular

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

object WorkMaster {

    private const val UNIQUE_WORK_NAME = "UniqueWorkName"

    /**
     * Enqueues a single work request with the provided parameters.
     *
     * @param context Application context.
     * @param workerClass Class of the worker.
     * @param inputData Input data for the worker (optional).
     * @param constraints Constraints for the work (optional).
     */
    fun enqueueSingleWork(
        context: Context,
        workerClass: Class<out ListenableWorker>,
        initialDelay: Long?=null,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        inputData: Data? = null,
        constraints: Constraints? = null,
    ) {
        val workRequestBuilder = OneTimeWorkRequest.Builder(workerClass).apply {
                inputData?.let { setInputData(it) }
                constraints?.let { setConstraints(it) }
                initialDelay?.let { setInitialDelay(initialDelay, timeUnit) }
            }

        val workRequest = workRequestBuilder.build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            UNIQUE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    /**
     * Enqueues a periodic work request with the provided parameters.
     *
     * @param context Application context.
     * @param workerClass Class of the worker.
     * @param repeatInterval Interval at which the work repeats.
     * @param inputData Input data for the worker (optional).
     * @param constraints Constraints for the work (optional).
     */
    fun enqueuePeriodicWork(
        context: Context,
        workerClass: Class<out ListenableWorker>,
        repeatInterval: Long,
        timeUnit: TimeUnit = TimeUnit.MINUTES,
        inputData: Data? = null,
        constraints: Constraints? = null
    ) {
        val workRequestBuilder = PeriodicWorkRequest.Builder(
            workerClass,
            repeatInterval,
            timeUnit
        )
        inputData?.let { workRequestBuilder.setInputData(it) }
        constraints?.let { workRequestBuilder.setConstraints(it) }

        val workRequest = workRequestBuilder.build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    /**
     * Cancels all work with the unique work name.
     *
     * @param context Application context.
     */
    fun cancelWork(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME)
    }

    /**
     * Cancels all ongoing work.
     *
     * @param context Application context.
     */
    fun cancelAllWork(context: Context) {
        WorkManager.getInstance(context).cancelAllWork()
    }

    /**
     * Checks the status of the unique work.
     *
     * @param context Application context.
     * @param callback Callback to handle work status.
     */
    fun getWorkStatus(
        context: Context,
        callback: (List<WorkInfo>) -> Unit
    ) {
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData(UNIQUE_WORK_NAME)
            .observeForever { workInfoList ->
                callback(workInfoList)
            }
    }
}
