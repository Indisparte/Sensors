package com.example.sensors

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


/**
 * @author Antonio Di Nuzzo (Indisparte)
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val lightSensor: MeasurableSensor,
    private val accelerometerSensor: AccelerometerSensor,
) : ViewModel() {
    private val TAG = MainViewModel::class.simpleName

    private val _isDark = MutableStateFlow(false)
    val isDark: StateFlow<Boolean> get() = _isDark
    private val _triple = MutableStateFlow<Triple<Float, Float, Float>?>(null)
    val triple: Flow<Triple<Float, Float, Float>?> get() = _triple

    init {
         lightSensor.startListening()
         lightSensor.setOnSensorValuesChangedListener { values ->
             val lux = values[0]
             _isDark.value = lux < 60f
         }
        accelerometerSensor.startListening()
        accelerometerSensor.setOnSensorValuesChangedListener { values ->
            val x = values[0]
            val y = values[1]
            val z = values[2]
            _triple.value = Triple(x,y,z)
        }
    }
}