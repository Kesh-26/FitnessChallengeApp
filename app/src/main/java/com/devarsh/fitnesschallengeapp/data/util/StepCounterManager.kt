package com.devarsh.fitnesschallengeapp.data.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StepCounterManager(
    context: Context
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepCounterSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _sensorValue = MutableStateFlow<Int?>(null)
    val sensorValue: StateFlow<Int?> = _sensorValue.asStateFlow()

    val isSensorAvailable: Boolean
        get() = stepCounterSensor != null

    fun start() {
        stepCounterSensor?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            _sensorValue.value = event.values[0].toInt()
        }
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {
        // Nothing required here
    }
}