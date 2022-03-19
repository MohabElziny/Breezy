package com.iti.mohab.breezy.manger

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.mohab.breezy.service.AlertService


class AlertOneTimeWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val description = inputData.getString("description")!!
        val icon = inputData.getString("icon")!!
        startAlertService(description, icon)
        return Result.success()
    }

    private fun startAlertService(description: String, icon: String) {
        val intent = Intent(applicationContext, AlertService::class.java)
        intent.putExtra("description", description)
        intent.putExtra("icon", icon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(applicationContext, intent)
        } else {
            applicationContext.startService(intent)
        }
    }
}