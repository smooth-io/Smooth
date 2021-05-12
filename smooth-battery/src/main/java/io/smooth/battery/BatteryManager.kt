package io.smooth.battery

import kotlinx.coroutines.flow.Flow

internal interface BatteryManager {

    fun listen(): Flow<BatteryState>

    fun getLevelState(): BatteryLevelState

    fun getChargingState(): BatteryChargingState

    fun getBatteryPercentage(): Float

}