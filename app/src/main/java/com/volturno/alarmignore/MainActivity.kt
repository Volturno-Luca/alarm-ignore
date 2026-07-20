package com.volturno.alarmignore

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Simple in-app screen with a big button that does the same thing as the widget,
 * plus a short explanation and a hint to add the home-screen widget.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.skip_button).setOnClickListener {
            AlarmSkipper.skipAllAlarms(this)
        }
    }
}
