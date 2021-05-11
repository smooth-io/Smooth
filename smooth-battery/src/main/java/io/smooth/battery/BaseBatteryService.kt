package io.smooth.battery

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onSubscription

abstract class BaseBatteryService(protected val context: Context) : BatteryManager {

    private val batteryListenerFlow: MutableSharedFlow<BatteryState> = MutableSharedFlow(0)

    init {
        batteryListenerFlow.onSubscription {
            trackBatteryEvents()
        }
    }

    override fun listen(): Flow<BatteryState> = batteryListenerFlow

    private fun trackBatteryEvents() {
        val subsCount = batteryListenerFlow.subscriptionCount.value
        if (subsCount > 0) listenForChangesInternal()
        else clearInternal()
    }

    protected suspend fun onChargingStateChanged(batteryChargingState: BatteryChargingState) {
        batteryListenerFlow.emit(
            ChargingStateChanged(batteryChargingState)
        )
    }

    protected suspend fun onBatteryLevelStateChanged(batteryLevelState: BatteryLevelState) {
        batteryListenerFlow.emit(
            LevelChanged(batteryLevelState)
        )
    }

    protected abstract fun clearInternal()

    protected abstract fun listenForChangesInternal()

}