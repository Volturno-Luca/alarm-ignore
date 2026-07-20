package com.volturno.alarmignore

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.AlarmClock
import android.widget.Toast

/**
 * Core logic: ask the system clock app to dismiss **all** alarms.
 *
 * For a repeating alarm, "dismiss" only skips its next scheduled ring — the alarm
 * stays enabled and rings again on its following day. For a one-off alarm it is
 * turned off. This is exactly the "skip today, keep for tomorrow" behaviour we want.
 *
 * See [AlarmClock.ACTION_DISMISS_ALARM] and [AlarmClock.ALARM_SEARCH_MODE_ALL].
 */
object AlarmSkipper {

    /**
     * Fire the dismiss-all intent. Must be started from an [Activity] context
     * because [AlarmClock.ACTION_DISMISS_ALARM] is an activity action.
     *
     * @return true if the clock app accepted the intent, false if no app handled it.
     */
    fun skipAllAlarms(activity: Activity): Boolean {
        val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM).apply {
            // Target every alarm, not just the next one.
            putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_ALL)
            // Don't open the clock app UI — this must feel like a single tap.
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }
        return try {
            activity.startActivity(intent)
            Toast.makeText(activity, R.string.toast_skipped, Toast.LENGTH_LONG).show()
            true
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, R.string.toast_no_clock_app, Toast.LENGTH_LONG).show()
            false
        }
    }
}
