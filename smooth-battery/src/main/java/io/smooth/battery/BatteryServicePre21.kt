package io.smooth.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal open class BatteryServicePre21(context: Context) : BaseBatteryService(context) {

    override fun getLevelState(): BatteryLevelState =
        getBatteryDetails()?.let {
            val perc = getBatteryPercentageFromIntent(it)
            if (perc > 15) BatteryLevelState.GOOD else BatteryLevelState.LOW
        } ?: BatteryLevelState.GOOD


    override fun getChargingState(): BatteryChargingState =
        getBatteryDetails()?.let {
            getBatteryChargingStatusFromIntent(it)
        } ?: BatteryChargingState.NOT_CHARGING

    override fun getBatteryPercentage(): Float = getBatteryDetails()?.let {
        getBatteryPercentageFromIntent(it)
    } ?: 0f

    override fun listenForChangesInternal() {
        val batteryLevelInternFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        context.registerReceiver(batteryBroadcastReceiver, batteryLevelInternFilter)
    }


    override fun clearInternal() {
        context.unregisterReceiver(batteryBroadcastReceiver)
    }

    private val batteryBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            GlobalScope.launch {
                intent?.apply {
                    when (intent.action) {
                        Intent.ACTION_BATTERY_LOW -> onBatteryLevelStateChanged(BatteryLevelState.LOW)
                        Intent.ACTION_BATTERY_OKAY -> onBatteryLevelStateChanged(BatteryLevelState.GOOD)
                        Intent.ACTION_POWER_CONNECTED -> onChargingStateChanged(BatteryChargingState.CHARGING)
                        Intent.ACTION_POWER_DISCONNECTED -> onChargingStateChanged(
                            BatteryChargingState.NOT_CHARGING
                        )
                    }
                }
            }
        }
    }


    protected fun getBatteryDetails(): Intent? {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        return context.registerReceiver(null, iFilter)
    }

    protected fun getBatteryPercentageFromIntent(intent: Intent): Float {
        val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return level * 100 / scale.toFloat()
    }

    private fun getBatteryChargingStatusFromIntent(intent: Intent): BatteryChargingState =
        getBatteryChargingStatusFromBatteryStatus(
            intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0)
        )

    protected fun getBatteryChargingStatusFromBatteryStatus(status: Int): BatteryChargingState {
        return when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> BatteryChargingState.CHARGING
            BatteryManager.BATTERY_STATUS_DISCHARGING -> BatteryChargingState.NOT_CHARGING
            BatteryManager.BATTERY_STATUS_FULL -> BatteryChargingState.CHARGING
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> BatteryChargingState.NOT_CHARGING
            else -> BatteryChargingState.NOT_CHARGING
        }
    }

}