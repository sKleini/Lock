package de.kleini.lock

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class App() : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "store"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Activity", "onCreate")
        setContentView(R.layout.activity_app)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        val sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        sharedPref.edit().putBoolean(PREF_NAME, false).apply()
        startLockTask()

        findViewById<View>(R.id.finish)
            .setOnClickListener {
                stopLockTask()
                finishAndRemoveTask()
            }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Activity", "onResume")

        val sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        if (sharedPref.getBoolean(PREF_NAME, true)) {
            stopLockTask()
            finishAndRemoveTask()
        } else {
            sharedPref.edit().putBoolean(PREF_NAME, true).apply()
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Activity", "onRestart")
        val sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        sharedPref.edit().putBoolean(PREF_NAME, false).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Activity", "onDestroy")
    }
}