package com.practice.work.dailyalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.work.WorkManager
import com.practice.util.date.DateUtil

class DailyNotificationAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.extras?.getInt(ALARM_REQUEST_KEY) == ALARM_REQUEST_CODE) {
            context?.let {
                val workManager = WorkManager.getInstance(context)
                DailyNotificationWork.setOneTimeDailyNotificationWork(workManager)
                setNextAlarm(context)
            }
        }
    }

    private fun setNextAlarm(context: Context) {
        setAlarm(context, SystemClock.elapsedRealtime() + 1000L * DAY_TO_SECONDS)
    }

    companion object {
        const val ALARM_REQUEST_KEY = "daily-alarm-request-key"
        const val ALARM_REQUEST_CODE = 900

        const val DAY_TO_SECONDS = 60 * 60 * 24

        private const val TAG = "DailyAlarmReceiver"

        fun setInitialAlarm(context: Context) {
            setAlarm(context, SystemClock.elapsedRealtime() + 1000 * getInitialDelayInSeconds())
        }

        private fun setAlarm(context: Context, triggerAtMillis: Long) {
            val intent = Intent(context, DailyNotificationAlarmReceiver::class.java).apply {
                putExtra(ALARM_REQUEST_KEY, ALARM_REQUEST_CODE)
            }
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                ALARM_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }

        private fun getInitialDelayInSeconds(): Long {
            return DateUtil.getNextTimeOffsetInSeconds(8, 0, 0)
        }
    }
}