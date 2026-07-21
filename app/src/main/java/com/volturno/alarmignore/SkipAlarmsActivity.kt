package com.volturno.alarmignore

import android.app.Activity
import android.os.Bundle

/**
 * Invisible trampoline started by the home-screen widget's one-tap PendingIntent.
 * It performs the dismiss-all, shows a toast, then immediately finishes so the user
 * never leaves their home screen.
 */
class SkipAlarmsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AlarmSkipper.skipTodaysAlarms(this) { finish() }
    }
}
