package com.puzzle.bench.themesalarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class KioskThemeAlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.getStringExtra(EXTRA_ID_THEM)?.let { idTheme ->
                Log.d(
                    KioskThemeAlarmReceiver::class.java.canonicalName,
                    "Apply KioskTheme $idTheme"
                )
                idThemeLiveData.postValue(idTheme)
            }
        }
    }

    companion object {
        const val KIOSK_THEME_ALARM_CODE = 1010
        const val EXTRA_ID_THEM = "EXTRA_ID_THEM"
        private val idThemeLiveData = MutableLiveData<String>()

        fun getIdThemeLiveData(): LiveData<String> {
            return idThemeLiveData
        }

    }
}