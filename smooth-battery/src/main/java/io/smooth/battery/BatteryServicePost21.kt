package io.smooth.battery

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager


open class BatteryServicePost21(context: Context) : BatteryServicePre21(context) {

    protected val batteryManager =
        context.getSystemService(BATTERY_SERVICE) as BatteryManager

    override fun getLevelState(): BatteryLevelState {
        val perc = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return if (perc > 15) BatteryLevelState.GOOD else BatteryLevelState.LOW
    }

    override fun getChargingState(): BatteryChargingState =
        getBatteryChargingStatusFromBatteryStatus(
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
        )

}