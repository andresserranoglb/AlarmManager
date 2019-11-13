package com.puzzle.bench.themesalarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class KioskThemeAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.getStringExtra(EXTRA_ID_THEM)?.let {
                Log.d(KioskThemeAlarmReceiver::class.java.canonicalName, "Apply KioskTheme $it")

            }
        }
    }
    companion object {
        const val EXTRA_ID_THEM = "EXTRA_ID_THEM"

    }
}