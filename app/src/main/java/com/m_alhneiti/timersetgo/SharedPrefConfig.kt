package com.m_alhneiti.timersetgo

import android.content.Context

object SharedPrefConfig {
    const val SHARED_PREF_NAME = "com.m_alhneiti.timersetgo.TSG_Preferences"

    const val PREF_SESSION_MINUTES = "sessionMinutes"
    const val PREF_SESSION_SECONDS = "sessionSeconds"

    const val PREF_BREAK_MINUTES = "breakMinutes"
    const val PREF_BREAK_SECONDS = "breakSeconds"

    fun getTimerValues(context: Context): Map<String, Int> {
        val preferencesMap = mutableMapOf<String, Int>()

        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        var prefValue = sharedPref.getInt(PREF_SESSION_MINUTES, 25)
        preferencesMap[PREF_SESSION_MINUTES] = prefValue

        prefValue = sharedPref.getInt(PREF_SESSION_SECONDS, 0)
        preferencesMap[PREF_SESSION_SECONDS] = prefValue

        prefValue = sharedPref.getInt(PREF_BREAK_MINUTES, 10)
        preferencesMap[PREF_BREAK_MINUTES] = prefValue

        prefValue = sharedPref.getInt(PREF_BREAK_SECONDS, 0)
        preferencesMap[PREF_BREAK_SECONDS] = prefValue

        return preferencesMap
    }//getTimerValues

    fun setTimerValues(context: Context, preferencesMap: Map<String, Int>) {
        val prefEditor = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit()

        prefEditor.putInt(PREF_SESSION_MINUTES, preferencesMap[PREF_SESSION_MINUTES]!!)
        prefEditor.putInt(PREF_SESSION_SECONDS, preferencesMap[PREF_SESSION_SECONDS]!!)

        prefEditor.putInt(PREF_BREAK_MINUTES, preferencesMap[PREF_BREAK_MINUTES]!!)
        prefEditor.putInt(PREF_BREAK_SECONDS, preferencesMap[PREF_BREAK_SECONDS]!!)

        prefEditor.apply()
        prefEditor.commit()
    }//setTimerValues

    fun clearTimerValues(context: Context) {
        val prefEditor = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit()

        prefEditor.clear()
        prefEditor.apply()
    }//clearTimerValues
}//SharedPrefConfig