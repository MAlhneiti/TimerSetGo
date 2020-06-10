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

    }//onCreate

    override fun onResume() {
        super.onResume()

        if(isFromSettings) getValuesFromPreferences()

    }//onResume

    private fun getValuesFromPreferences() {
        val sharedPref = this.getSharedPreferences(SharedPrefConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        sharedSessionMin = sharedPref.getInt(SharedPrefConfig.PREF_SESSION_MINUTES, 25)
        sharedSessionSec = sharedPref.getInt(SharedPrefConfig.PREF_SESSION_SECONDS, 0)

        sharedBreakMin = sharedPref.getInt(SharedPrefConfig.PREF_BREAK_MINUTES, 10)
        sharedBreakSec = sharedPref.getInt(SharedPrefConfig.PREF_BREAK_SECONDS, 0)
    }//getValuesFromPreferences

//    private fun loadConfetti() {
//        viewKonfetti.build()
//            .addColors(Color.YELLOW, Color.WHITE, Color.MAGENTA)
//            .setDirection(0.0, 359.0)
//            .setSpeed(1f, 5f)
//            .setFadeOutEnabled(true)
//            .setTimeToLive(2000L)
//            .addShapes(Shape.RECT, Shape.CIRCLE)
//            .addSizes(Size(12))
//            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
//            .streamFor(300, 5000L)
//    }//loadConfetti

}//MainActivity
