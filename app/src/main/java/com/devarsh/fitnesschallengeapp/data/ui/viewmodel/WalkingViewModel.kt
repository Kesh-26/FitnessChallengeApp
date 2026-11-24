package com.devarsh.fitnesschallengeapp.data.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devarsh.fitnesschallengeapp.data.entity.DailySteps
import com.devarsh.fitnesschallengeapp.data.repository.DailyStepsRepository
import com.devarsh.fitnesschallengeapp.data.util.StepCounterManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WalkingViewModel(
    application: Application,
    private val dailyStepsRepository: DailyStepsRepository
) : AndroidViewModel(application) {

    private val stepCounterManager =
        StepCounterManager(application.applicationContext)

    private val _stepsToday = MutableStateFlow(0)
    val stepsToday: StateFlow<Int> = _stepsToday.asStateFlow()

    private val _sensorAvailable =
        MutableStateFlow(stepCounterManager.isSensorAvailable)

    val sensorAvailable: StateFlow<Boolean> =
        _sensorAvailable.asStateFlow()

    private val _isTracking =
        MutableStateFlow(false)

    val isTracking: StateFlow<Boolean> =
        _isTracking.asStateFlow()

    private val today: String
        get() = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())

    init {
        observeSensor()
        observeStoredSteps()
    }

    fun startTracking() {
        if (!stepCounterManager.isSensorAvailable) {
            _sensorAvailable.value = false
            return
        }

        _sensorAvailable.value = true
        _isTracking.value = true

        stepCounterManager.start()
    }

    fun stopTracking() {
        stepCounterManager.stop()
        _isTracking.value = false
    }

    private fun observeStoredSteps() {
        viewModelScope.launch {
            dailyStepsRepository
                .getStepsForDate(today)
                .collectLatest { dailySteps ->

                    _stepsToday.value =
                        dailySteps?.stepsToday ?: 0
                }
        }
    }

    private fun observeSensor() {
        viewModelScope.launch {

            stepCounterManager.sensorValue.collectLatest { sensorValue ->

                if (sensorValue == null) {
                    return@collectLatest
                }

                val date = today

                val existing =
                    dailyStepsRepository
                        .getStepsForDateOnce(date)

                if (existing == null) {

                    // First sensor reading of the day becomes the baseline
                    val newDailySteps = DailySteps(
                        date = date,
                        sensorBaseline = sensorValue,
                        currentSensorValue = sensorValue,
                        stepsToday = 0
                    )

                    dailyStepsRepository.saveSteps(newDailySteps)

                } else {

                    var baseline =
                        existing.sensorBaseline

                    /*
                     * If the phone was rebooted, TYPE_STEP_COUNTER
                     * may become smaller than the previous baseline.
                     * Reset the baseline safely.
                     */
                    if (sensorValue < baseline) {
                        baseline = sensorValue
                    }

                    val calculatedSteps =
                        (sensorValue - baseline)
                            .coerceAtLeast(0)

                    val updated =
                        existing.copy(
                            sensorBaseline = baseline,
                            currentSensorValue = sensorValue,
                            stepsToday = calculatedSteps
                        )

                    dailyStepsRepository.saveSteps(updated)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stepCounterManager.stop()
    }
}