package com.iti.mohab.breezy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iti.mohab.breezy.dialogs.InitialSettingDialog
import com.iti.mohab.breezy.util.getSharedPreferences
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val parentJob = Job()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (isFirstTime()) {
            InitialSettingDialog().show(supportFragmentManager, "InitialFragment")
        } else {
            val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
            coroutineScope.launch {
                delay(4000)
                startMainScreen()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun startMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        parentJob.cancel()
    }

    private fun isFirstTime(): Boolean {
        return getSharedPreferences(this).getBoolean("firstTime", true)
    }
}