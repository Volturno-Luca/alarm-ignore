package com.volturno.alarmignore

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.AlarmClock
import android.widget.Toast

/**
 * Core logic: ask the system clock app to skip the **next** alarm.
 *
 * Google Clock does not honour a silent "dismiss ALL" request — with
 * [AlarmClock.ALARM_SEARCH_MODE_ALL] it just opens the alarm list instead of
 * skipping. Targeting the single next alarm with [AlarmClock.ALARM_SEARCH_MODE_NEXT]
 * is unambiguous, which is what the clock needs to perform the skip silently.
 *
 * For a repeating alarm, "dismiss" only skips its next scheduled ring — the alarm
 * stays enabled and rings again on its following day. This is the "skip today,
 * keep for tomorrow" behaviour we want.
 */
object AlarmSkipper {

    /**
     * Fire the dismiss intent for the next alarm. Must be started from an
     * [Activity] context because [AlarmClock.ACTION_DISMISS_ALARM] is an
     * activity action.
     *
     * @return true if the clock app accepted the intent, false if no app handled it.
     */
    fun skipNextAlarm(activity: Activity): Boolean {
        val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM).apply {
            // Target only the next alarm — an unambiguous request the clock can
            // act on without popping its UI.
            putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_NEXT)
            // Don't open the clock app UI — this must feel like a single tap.
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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
