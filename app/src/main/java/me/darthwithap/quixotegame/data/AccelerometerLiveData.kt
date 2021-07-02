package me.darthwithap.quixotegame.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LiveData
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

private const val TAG = "AccelerometerLiveData"

class AccelerometerInclinationLiveData(
    private val sensorManager: SensorManager? = null,
) : LiveData<Int?>(), SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        val g = event?.values?.clone()
        val normalization = g?.let {
            sqrt((it[0] * it[0] + it[1] * it[1] + it[2] * it[2]).toDouble())
        }
        normalization?.let {
            g[0] /= it.toFloat()
            g[1] /= it.toFloat()
            g[2] /= it.toFloat()
        }
        /* inclination < 15 or > 165 horizontal */
        /* 80 < inclination < 100  vertical*/
        val inclination =
            g?.get(2)?.toDouble()?.let { acos(it) }?.let { Math.toDegrees(it).roundToInt() }
        val rotation =
            g?.get(1)?.let { atan2(g[0], it).toDouble() }
                ?.let { Math.toDegrees(it).roundToInt() }
        Log.d(TAG, "onSensorChanged: Rotation: $rotation")

        postValue(inclination)
    }

    override fun onActive() {
        sensorManager?.let { sm ->
            sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).let {
                sm.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    override fun onInactive() {
        sensorManager?.unregisterListener(this)
    }
}