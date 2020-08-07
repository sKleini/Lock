package de.kleini.lock

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class App() : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "store"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Activity", "onCreate")
        setContentView(R.layout.activity_app)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
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

        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

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
        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        sharedPref.edit().putBoolean(PREF_NAME, false).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Activity", "onDestroy")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

}