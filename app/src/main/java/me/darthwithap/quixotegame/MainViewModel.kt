package me.darthwithap.quixotegame

import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import me.darthwithap.quixotegame.data.AccelerometerInclinationLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val sensorManager: SensorManager
        get() = getApplication<Application>().getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val inclinationLiveData = AccelerometerInclinationLiveData(sensorManager)

}