package io.smooth.battery

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.flow.Flow


class SmoothBatteryService(
    context: Context
) : BatteryManager {

    @SuppressLint("ObsoleteSdkInt")
    private val internalBatteryManager: BatteryManager =
        when {
            android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP -> {
                BatteryServicePre21(context)
            }
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP -> {
                BatteryServicePost21(context)
            }
            else -> BatteryServicePost28(context)
        }

    override fun listen(): Flow<BatteryState> = internalBatteryManager.listen()

    override fun getLevelState(): BatteryLevelState =
        internalBatteryManager.getLevelState()

    override fun getChargingState(): BatteryChargingState =
        internalBatteryManager.getChargingState()

    override fun getBatteryPercentage(): Float =
        internalBatteryManager.getBatteryPercentage()

    companion object {
        private var smoothBatteryService: SmoothBatteryService? = null

        internal fun init(context: Context): SmoothBatteryService {
            smoothBatteryService =
                SmoothBatteryService(context)
            return smoothBatteryService!!
        }

        fun getInstance(): SmoothBatteryService {
            if (smoothBatteryService == null) {
                throw IllegalArgumentException("Must call init for Battery manager")
            }
            return smoothBatteryService!!
        }
    }


}