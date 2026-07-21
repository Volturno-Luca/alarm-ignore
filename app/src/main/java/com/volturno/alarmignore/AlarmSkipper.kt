package com.volturno.alarmignore

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.AlarmClock
import android.widget.Toast

/**
 * EXPERIMENT (non-silent): ask the clock to dismiss the next alarm **without**
 * [AlarmClock.EXTRA_SKIP_UI], letting Google Clock open and run the action
 * itself. The silent path removed the "skip" button but didn't stop the ring;
 * this checks whether the non-silent path performs the real skip.
 */
object AlarmSkipper {

    fun skipTodaysAlarms(activity: Activity, onFinished: () -> Unit) {
        val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM).apply {
            putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_NEXT)
            // Deliberately NO EXTRA_SKIP_UI — let the clock app show and act.
        }
        val message = try {
            activity.startActivity(intent)
            R.string.toast_sent
        } catch (e: ActivityNotFoundException) {
            R.string.toast_no_clock_app
        }
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        onFinished()
    }
}
