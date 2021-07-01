package me.darthwithap.quixotegame

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.getSystemService
import kotlin.math.acos
import kotlin.math.roundToInt
import kotlin.math.sqrt

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private lateinit var accelerometerSensorEventListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService()!!
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        accelerometerSensorEventListener = object : SensorEventListener {
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
                val inclination =
                    g?.get(2)?.toDouble()?.let { acos(it) }?.let { Math.toDegrees(it).roundToInt() }
                Log.d(TAG, "onSensorChanged: x: ${g?.get(0)}")
                Log.d(TAG, "onSensorChanged: x: ${g?.get(1)}")
                Log.d(TAG, "onSensorChanged: x: ${g?.get(2)}")

                Log.d(TAG, "onSensorChanged: Inclination: $inclination")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            accelerometerSensorEventListener,
            accelerometerSensor,
            1000 * 1000 * 2
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(accelerometerSensorEventListener, accelerometerSensor)
    }
}