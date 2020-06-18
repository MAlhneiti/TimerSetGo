package com.m_alhneiti.timersetgo

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var sharedSessionMin:Int = 25
    private var sharedSessionSec:Int = 0
    private var sharedBreakMin:Int = 10
    private var sharedBreakSec:Int = 0

    private var numberOfSessions: Int = 1
    private var sessionNumber:Int = 0

    private var timeInMilliseconds: Long = 0

    private var isFromSettings: Boolean = false
    private var isBreak: Boolean = false
    private var isNumOfSessionsSet: Boolean = true
    private var isFinished: Boolean = false
    private var hasBreaks: Boolean = true

    private var timerState: TimerState = TimerState.STOPPED

    private lateinit var countDownTimer: CountDownTimer

    private lateinit var timerMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setting the animated background
        val animDrawable = Main_root_layout.background as AnimationDrawable

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        animDrawable.setEnterFadeDuration(1000)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()

        PB_countdown.rotationY = 180.0F

        //Getting values from SharedPreferences
        getValuesFromPreferences()
        initTimerUI()

        if(sharedBreakMin == 0 && sharedBreakSec == 0) {
            hasBreaks = false
        }

        isFromSettings = intent.getBooleanExtra("isFromSettings", false)

        timerMediaPlayer = MediaPlayer.create(this, R.raw.digital_watch_alarm_long)

        //Settings button Listener
        BTN_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)

            if(timerState != TimerState.RUNNING) {
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.timer_is_running), Toast.LENGTH_SHORT).show()
            }
        }//BTN_settings

        BTN_set.setOnClickListener {
            numberOfSessions = ET_sessionNumberInput.text.toString().trim().toInt()

            if(timerState == TimerState.RUNNING) {
                Toast.makeText(this, getString(R.string.please_stop_or_pause), Toast.LENGTH_SHORT).show()
            } else if (numberOfSessions <= 0) {
                Toast.makeText(this, getString(R.string.invalid_number), Toast.LENGTH_SHORT).show()
                isNumOfSessionsSet = false
            } else {
                updateSessionNumber()
                isNumOfSessionsSet = true
            }
        }//BTN_set

        BTN_start.setOnClickListener {
            if(timerState != TimerState.RUNNING) {
                if(isNumOfSessionsSet) {
                    if(isFinished) {
                        initTimerUI()
                    }

                    setTimeInMilliseconds()

                    startTimer()
                } else {
                    Toast.makeText(this, getString(R.string.please_enter_valid_number), Toast.LENGTH_SHORT).show()
                }
            }
        }//BTN_start

        BTN_pause.setOnClickListener {
            if(timerState == TimerState.RUNNING){
                countDownTimer.cancel()
                timerState = TimerState.PAUSED
            }
        }//BTN_pause

        BTN_stop.setOnClickListener {
            if(timerState == TimerState.RUNNING || timerState == TimerState.PAUSED) {
                countDownTimer.cancel()

                timerState = TimerState.STOPPED
                initTimerUI()
            }
        }//BTN_stop

        BTN_stopAlarm.setOnClickListener {
            if(timerMediaPlayer.isPlaying) {
                timerMediaPlayer.stop()
                timerMediaPlayer = MediaPlayer.create(this, R.raw.digital_watch_alarm_long)
                BTN_stopAlarm.visibility = View.GONE
            }
        }//BTN_stopAlarm
    }//onCreate

    override fun onResume() {
        super.onResume()

        if(isFromSettings) {
            getValuesFromPreferences()
            initTimerUI()
            isFromSettings = false
        }
    }//onResume

    override fun onDestroy() {
        super.onDestroy()

        if(timerMediaPlayer.isPlaying) {
            timerMediaPlayer.stop()
        }
    }//onDestroy

    private fun getValuesFromPreferences() {
        val preferencesMap = SharedPrefConfig.getTimerValues(this)

        sharedSessionMin = preferencesMap[SharedPrefConfig.PREF_SESSION_MINUTES]!!
        sharedSessionSec = preferencesMap[SharedPrefConfig.PREF_SESSION_SECONDS]!!
        sharedBreakMin = preferencesMap[SharedPrefConfig.PREF_BREAK_MINUTES]!!
        sharedBreakSec = preferencesMap[SharedPrefConfig.PREF_BREAK_SECONDS]!!
    }//getValuesFromPreferences

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeInMilliseconds, 1000) {

            override fun onFinish() {
                timerState = TimerState.STOPPED

                //timerMediaPlayer.setVolume(1.0F, 1.0F)

                if(hasBreaks) {
                    if(!isBreak) {
                        sessionNumber++
                        timerMediaPlayer.start()
                        BTN_stopAlarm.visibility = View.VISIBLE
                    }
                } else {
                    sessionNumber++
                    timerMediaPlayer.start()
                    BTN_stopAlarm.visibility = View.VISIBLE
                }

                if(sessionNumber >= numberOfSessions) {
                    Toast.makeText(this@MainActivity, getString(R.string.finished_sessions), Toast.LENGTH_SHORT).show()
                    isFinished = true
                    resetTimer()
                } else {
                    if(hasBreaks) {
                        if(isBreak) {
                            isBreak = false
                            resetTimer()
                            startTimer()
                        } else {
                            isBreak = true
                            resetTimer()
                            startTimer()
                        }//else
                    } else {
                        resetTimer()
                        startTimer()
                    }
                }//else
            }//onFinish

            override fun onTick(millisUntilFinished: Long) {
                timerState = TimerState.RUNNING

                PB_countdown.progress = millisUntilFinished.toInt()

                val minutesUntilFinished = ((millisUntilFinished/60000) % 60).toString().padStart(2, '0')
                val secondsUntilFinished = ((millisUntilFinished/1000) % 60).toString().padStart(2, '0')

                val timerText = "$minutesUntilFinished:$secondsUntilFinished"

                TV_timer.text = timerText

                timeInMilliseconds = millisUntilFinished
            }//onTick

        }.start() //CountDownTimer
    }//startTimer

    private fun initTimerUI() {
        sessionNumber = 0
        isBreak = false
        isFinished = false

        setTimeInMilliseconds()
        updateSessionNumber()
        updateSessionText()
        updateTimerUI()
    }//initTimerUI

    private fun setTimeInMilliseconds() {
        if(timerState != TimerState.PAUSED) {
            timeInMilliseconds = (if(!isBreak) {
                (sharedSessionMin * 60000) + (sharedSessionSec*1000)
            } else {
                (sharedBreakMin*60000) + (sharedBreakSec*1000)
            }).toLong()

            PB_countdown.max = timeInMilliseconds.toInt()
            PB_countdown.progress = timeInMilliseconds.toInt()
        }
    }//setTimeInMilliseconds

    private fun resetTimer() {
        updateSessionNumber()
        updateSessionText()
        updateTimerUI()
        setTimeInMilliseconds()
    }//resetTimer

    private fun updateSessionText() {
        if(isFinished) {
            TV_sessionTypeText.text = getString(R.string.is_finished)
        } else {
            if(isBreak)
                TV_sessionTypeText.text = getString(R.string.break_time)
            else
                TV_sessionTypeText.text = getString(R.string.work_hard)
        }
    }//updateSessionText

    private fun updateSessionNumber() {
        val sessionNumberText = "Session: $sessionNumber / $numberOfSessions"
        TV_sessionNumber.text = sessionNumberText
    }//updateSessionNumber

    private fun updateTimerUI() {
        val minutes: String
        val seconds: String

        if(isBreak) {
            minutes = sharedBreakMin.toString().padStart(2, '0')
            seconds = sharedBreakSec.toString().padStart(2, '0')
        } else {
            minutes = sharedSessionMin.toString().padStart(2, '0')
            seconds = sharedSessionSec.toString().padStart(2, '0')
        }

        val timerText = "$minutes:$seconds"
        TV_timer.text = timerText
    }//updateTimerUI
}//MainActivity
