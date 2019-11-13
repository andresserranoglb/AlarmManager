package com.puzzle.bench.themesalarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.puzzle.bench.themesalarmmanager.KioskThemeAlarmReceiver.Companion.EXTRA_ID_THEM
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_set_alarm.setOnClickListener {
            if (tv_hour.text.isEmpty() || tv_minute.text.isEmpty()) {
                return@setOnClickListener
            }
            val hour = tv_hour.text.toString().toInt()
            val minutes = tv_minute.text.toString().toInt()
            val kioskTheme = KioskTheme(hour, minutes, "1")
            val kioskTheme2 = KioskTheme(hour, minutes + 1, "2")
            val alarHelper = AlarmManagerHelper(it.context)
            alarHelper.run {
                createAlarm(kioskTheme)
                createAlarm(kioskTheme2)
            }
        }
    }

    private fun createAlarmSetExactAndAllowWhileIdle(context: Context, kioskTheme: KioskTheme) {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, kioskTheme.hour)
            set(Calendar.MINUTE, kioskTheme.minute)
            set(Calendar.SECOND, 0)
        }
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, KioskThemeAlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                calendar.timeInMillis.toInt(),
                intent.putExtra(EXTRA_ID_THEM, kioskTheme.idTheme),
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
}
