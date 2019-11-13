package com.puzzle.bench.themesalarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.puzzle.bench.themesalarmmanager.KioskThemeAlarmReceiver.Companion.EXTRA_ID_THEM
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var alarHelper: AlarmManagerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_set_alarm.setOnClickListener {
            if (tv_hour.text.isEmpty() || tv_minute.text.isEmpty()) {
                createAlarm(it.context)
                return@setOnClickListener
            }
            val hour = tv_hour.text.toString().toInt()
            val minutes = tv_minute.text.toString().toInt()
            val kioskTheme = KioskTheme(hour, minutes, "1")
            val kioskTheme2 = KioskTheme(hour, minutes + 1, "2")
            alarHelper = AlarmManagerHelper(it.context)
            createAlarm(it.context)
            alarHelper.run {
                setKioskThemeAlarm(kioskTheme)
                //setKioskThemeAlarm(kioskTheme2)
            }
            subscribeToKioskReceiver()
        }
        bt_cancel_alarm.setOnClickListener {
            alarHelper.cancelAlarm()
        }

    }


    override fun onStop() {
        super.onStop()
        alarHelper.cancelAlarm()

    }

    private fun subscribeToKioskReceiver() {
        KioskThemeAlarmReceiver.getIdThemeLiveData().observe(this, Observer {
            Toast.makeText(this, "Apply theme $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun createAlarm(context: Context) {

        val seconds = TimeUnit.SECONDS.toMillis(20)
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime() + seconds,
            Intent(context, KioskThemeAlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(
                    context,
                    KioskThemeAlarmReceiver.KIOSK_THEME_ALARM_CODE,
                    intent.putExtra(EXTRA_ID_THEM, "test"),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        )

        Log.d(
            MainActivity::class.java.canonicalName,
            "createAlarm minuteMillis"
        )
    }
}
