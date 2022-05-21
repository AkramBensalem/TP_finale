package dz.akram.bensalem.tp.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.sqrt


object SensorUtils{

    fun detectCheckPhone(
        sensorManager : SensorManager,
        acceleration : Float = 10f,
        currentAcceleration : Float = SensorManager.GRAVITY_EARTH,
        lastAcceleration : Float = SensorManager.GRAVITY_EARTH,
        onChecking : (Boolean) -> Unit,
        onCurrentAccelerationChanged : (Float) -> Unit,
        onLastAccelerationChanged : (Float) -> Unit,
        onAccelerationChanged : (Float) -> Unit
    ){
     //

        val sensorShake: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensorEventListener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                onLastAccelerationChanged(currentAcceleration)

                val x = sensorEvent.values[0]
                val y = sensorEvent.values[1]
                val z = sensorEvent.values[2]
                val floatSum = abs(x) + abs(y) + abs(z)

                onCurrentAccelerationChanged(sqrt((x * x + y * y + z * z).toDouble()).toFloat())
                val delta: Float = currentAcceleration - lastAcceleration
                onAccelerationChanged(acceleration * 0.9f + delta)
                onChecking(floatSum > 32)
            }

            override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}
        }
        sensorManager.registerListener(
            sensorEventListener,
            sensorShake,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }


}