package io.smooth.battery

sealed class BatteryState
data class ChargingStateChanged(
    val batteryChargingState: BatteryChargingState
) : BatteryState()

data class LevelChanged(
    val levelState: BatteryLevelState
) : BatteryState()
