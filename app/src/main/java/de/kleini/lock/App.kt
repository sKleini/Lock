package de.kleini.lock

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class App : AppCompatActivity() {

    private val TAG = "App"
    private val viewModel: LockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_app)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        viewModel.setShouldExit(false)

        if (!checkDrawOverlayPermission()) {
            startLockTask()
        }

        findViewById<View>(R.id.finish)
            .setOnClickListener {
                if (!checkDrawOverlayPermission()) {
                    stopLockTask()
                }
                finishAndRemoveTask()
            }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")

        if (viewModel.shouldExit.value) {
            if (!checkDrawOverlayPermission()) {
                stopLockTask()
            }
            finishAndRemoveTask()
        } else {
            viewModel.setShouldExit(true)
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
        viewModel.setShouldExit(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    private fun checkDrawOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}
