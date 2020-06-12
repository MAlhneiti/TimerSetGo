package com.m_alhneiti.timersetgo

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private var hasChangedValues = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //Setting the animated background
        val animDrawable = Settings_root_layout.background as AnimationDrawable

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        animDrawable.setEnterFadeDuration(1000)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()

        initNumberPickers()

        //Setting Save onClickListener
        BTN_save.setOnClickListener {
            setValuesToPreferences()
        }//BTN_save

        BTN_back.setOnClickListener {
            goBackToMainActivity()
        }//BTN_back

        BTN_clear.setOnClickListener {
            clearSavedValuesInPreferences()
        }//BTN_clear
    }//onCreate

    private fun initNumberPickers() {
        //Setting min, max, and initial values of all NumberPickers
        SessionTime_minutes.minValue = 1
        SessionTime_minutes.maxValue = 60
        SessionTime_minutes.value = 25

        SessionTime_seconds.minValue = 0
        SessionTime_seconds.maxValue = 60
        SessionTime_seconds.value = 0

        BreakTime_minutes.minValue = 1
        BreakTime_minutes.maxValue = 60
        BreakTime_minutes.value = 10

        BreakTime_seconds.minValue = 0
        BreakTime_seconds.maxValue = 60
        BreakTime_seconds.value = 0
    }//initNumberPickers

    private fun setValuesToPreferences() {
        val preferencesMap = mutableMapOf<String, Int>()

        preferencesMap[SharedPrefConfig.PREF_SESSION_MINUTES] = SessionTime_minutes.value
        preferencesMap[SharedPrefConfig.PREF_SESSION_SECONDS] = SessionTime_seconds.value
        preferencesMap[SharedPrefConfig.PREF_BREAK_MINUTES] = BreakTime_minutes.value
        preferencesMap[SharedPrefConfig.PREF_BREAK_SECONDS] = BreakTime_seconds.value

        SharedPrefConfig.setTimerValues(this, preferencesMap)

        hasChangedValues = true

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()

        goBackToMainActivity()
    }//saveValuesToPreferences

    private fun goBackToMainActivity() {
        if(hasChangedValues) {
            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra("isFromSettings", true)

            startActivity(intent)
        } else {
            finish()
        }
    }//goBackToMainActivity

    private fun clearSavedValuesInPreferences() {
        SharedPrefConfig.clearTimerValues(this)

        SessionTime_minutes.value = 25
        SessionTime_seconds.value = 0
        BreakTime_minutes.value = 10
        BreakTime_seconds.value = 0

        hasChangedValues = true

        Toast.makeText(this, "Cleared!", Toast.LENGTH_SHORT).show()

        goBackToMainActivity()
    }//clearSavedValuesInPreferences

}//SettingsActivity
