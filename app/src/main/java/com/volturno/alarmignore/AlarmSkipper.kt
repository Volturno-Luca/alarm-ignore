package com.volturno.alarmignore

import android.app.Activity
import android.app.AlarmManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import android.widget.Toast
import java.util.Calendar

/**
 * Skips every alarm scheduled for **today**, one tap, leaving them enabled for
 * the following days.
 *
 * Google Clock ignores a silent "dismiss ALL" (it just opens the alarm list),
 * but it does honour a silent dismiss of the **next** alarm
 * ([AlarmClock.ALARM_SEARCH_MODE_NEXT] + [AlarmClock.EXTRA_SKIP_UI]). So we skip
 * the next alarm, wait for the clock to reschedule, look at the new next-alarm
 * time via [AlarmManager.getNextAlarmClock], and repeat — stopping as soon as
 * the next alarm rolls over to tomorrow, so future days are never touched.
 */
object AlarmSkipper {

    /** Delay between skips, giving the clock app time to reschedule its next alarm. */
    private const val STEP_DELAY_MS = 1000L

    /** Hard safety cap on how many alarms we will skip in one run. */
    private const val MAX_ALARMS = 25

    /**
     * @param onFinished called once all of today's alarms have been skipped
     *                   (used by the invisible widget trampoline to finish()).
     */
    fun skipTodaysAlarms(activity: Activity, onFinished: () -> Unit) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val handler = Handler(Looper.getMainLooper())
        val endOfToday = endOfTodayMillis()
        var skipped = 0

        fun done() {
            val message = if (skipped == 0) {
                activity.getString(R.string.toast_none)
            } else {
                activity.resources.getQuantityString(R.plurals.toast_skipped_count, skipped, skipped)
            }
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            onFinished()
        }

        // Trigger time of the next alarm, but only if it rings before midnight.
        fun nextAlarmToday(): Long? {
            val trigger = alarmManager.nextAlarmClock?.triggerTime ?: return null
            return if (trigger < endOfToday) trigger else null
        }

        fun step(previousTrigger: Long) {
            handler.postDelayed({
                val trigger = nextAlarmToday()
                // Stop when the next alarm is tomorrow (null) or the last skip
                // didn't move the pointer (clock refused), or we hit the cap.
                if (trigger == null || trigger == previousTrigger || skipped >= MAX_ALARMS) {
                    done()
                    return@postDelayed
                }
                dismissNext(activity)
                skipped++
                step(trigger)
            }, STEP_DELAY_MS)
        }

        val first = nextAlarmToday()
        if (first == null) {
            done()
            return
        }
        if (!dismissNext(activity)) {
            Toast.makeText(activity, R.string.toast_no_clock_app, Toast.LENGTH_LONG).show()
            onFinished()
            return
        }
        skipped++
        step(first)
    }

    /**
     * Ask the clock app to skip the next alarm, silently.
     * @return false only if no app handled the intent.
     */
    private fun dismissNext(activity: Activity): Boolean {
        val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM).apply {
            putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_NEXT)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        return try {
            activity.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    /** Local midnight at the start of tomorrow — the boundary of "today". */
    private fun endOfTodayMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.add(Calendar.DAY_OF_YEAR, 1)
        return cal.timeInMillis
    }
}
