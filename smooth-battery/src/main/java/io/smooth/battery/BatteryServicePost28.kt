package io.smooth.battery

import android.content.Context
import android.os.BatteryManager

internal class BatteryServicePost28(context: Context) : BatteryServicePost21(context) {

    override fun getLevelState(): BatteryLevelState {
        val isLow = getBatteryDetails()?.getBooleanExtra(
            BatteryManager.EXTRA_BATTERY_LOW, true
        ) ?: true
        return if (!isLow) BatteryLevelState.GOOD else BatteryLevelState.LOW
    }

}