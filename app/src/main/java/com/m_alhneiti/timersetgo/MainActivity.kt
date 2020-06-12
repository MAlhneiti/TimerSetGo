package com.m_alhneiti.timersetgo

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var sharedSessionMin:Int = 0
    private var sharedSessionSec:Int = 0
    private var sharedBreakMin:Int = 0
    private var sharedBreakSec:Int = 0

    private var isFromSettings:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PB_countdown.max = 100

        //Setting the animated background
        val animDrawable = Main_root_layout.background as AnimationDrawable

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        animDrawable.setEnterFadeDuration(1000)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()

        //Getting values from SharedPreferences
        getValuesFromPreferences()

        isFromSettings = intent.getBooleanExtra("isFromSettings", false)

        //Settings button Listener
        BTN_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }//BTN_settings

        BTN_start.setOnClickListener {
            PB_countdown.progress = 50
        }//BTN_start

        BTN_pause.setOnClickListener {
            PB_countdown.progress = 75
        }//BTN_pause

        BTN_stop.setOnClickListener {
            PB_countdown.progress = 0
        }//BTN_stop
    }//onCreate

    override fun onResume() {
        super.onResume()

        if(isFromSettings) getValuesFromPreferences()

    }//onResume

    private fun getValuesFromPreferences() {
        val preferencesMap = SharedPrefConfig.getTimerValues(this)

        sharedSessionMin = preferencesMap[SharedPrefConfig.PREF_SESSION_MINUTES]!!
        sharedSessionSec = preferencesMap[SharedPrefConfig.PREF_SESSION_SECONDS]!!
        sharedBreakMin = preferencesMap[SharedPrefConfig.PREF_BREAK_MINUTES]!!
        sharedBreakSec = preferencesMap[SharedPrefConfig.PREF_BREAK_SECONDS]!!
    }//getValuesFromPreferences

}//MainActivity
