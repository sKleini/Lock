package de.kleini.lock

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class App() : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "store"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        val sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        findViewById<View>(R.id.finish)
            .setOnClickListener {
                sharedPref.edit().putBoolean(PREF_NAME, true).apply()

                stopLockTask()
                finishAndRemoveTask()
            }
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        if (sharedPref.getBoolean(PREF_NAME, true)) {
            sharedPref.edit().putBoolean(PREF_NAME, false).apply()

            startLockTask()
        } else {
            sharedPref.edit().putBoolean(PREF_NAME, true).apply()

            stopLockTask()
            finishAndRemoveTask()
        }
    }

}