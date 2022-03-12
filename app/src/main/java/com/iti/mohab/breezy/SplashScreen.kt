package com.iti.mohab.breezy

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iti.mohab.breezy.dialogs.InitialSettingDialog
import com.iti.mohab.breezy.util.getSharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (isFirstTime()) {
            InitialSettingDialog().show(supportFragmentManager, "InitialFragment")
        } else {
            runBlocking {
                delay(4000)
                startMainScreen()
            }
        }


//        val handler = Handler(Looper.getMainLooper())
//
//        handler.postDelayed({
//
//        }, 4000)
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

    private fun isFirstTime(): Boolean {
        return getSharedPreferences(this).getBoolean("firstTime", true)
    }
}