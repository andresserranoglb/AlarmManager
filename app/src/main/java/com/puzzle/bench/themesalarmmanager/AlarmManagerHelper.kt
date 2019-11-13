package com.puzzle.bench.themesalarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.puzzle.bench.themesalarmmanager.KioskThemeAlarmReceiver.Companion.KIOSK_THEME_ALARM_CODE
import java.util.*

class AlarmManagerHelper(private val context: Context) {
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private lateinit var intent: PendingIntent


    fun setKioskThemeAlarm(kioskTheme: KioskTheme) {
        cancelAlarm()
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, kioskTheme.hour)
            set(Calendar.MINUTE, kioskTheme.minute)
            set(Calendar.SECOND, 0)
        }
        intent = Intent(context, KioskThemeAlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                KIOSK_THEME_ALARM_CODE,
                intent.putExtra(KioskThemeAlarmReceiver.EXTRA_ID_THEM, kioskTheme.idTheme),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        Log.d(
            MainActivity::class.java.canonicalName,
            "Start  AlarmManager for KioskTheme ${kioskTheme.idTheme}"
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                intent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                intent
            )
        }
    }

    fun cancelAlarm() {
        if (::intent.isInitialized) {
            alarmManager.cancel(intent)
            Log.d(
                MainActivity::class.java.canonicalName,
                " AlarmManager for KioskTheme cancelled "
            )
        }

    }
}