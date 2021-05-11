package io.smooth.battery.starter

import android.content.Context
import androidx.startup.Initializer
import io.smooth.battery.SmoothBatteryService

class BatteryManagerInitializer : Initializer<SmoothBatteryService> {

    override fun create(context: Context): SmoothBatteryService =
        SmoothBatteryService.init(context)
   
    override fun dependencies(): MutableList<Class<out Initializer<*>>> = arrayListOf()

}