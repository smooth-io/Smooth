package io.smooth.constraint.android

import io.smooth.battery.*
import io.smooth.constraint.Constraint
import io.smooth.constraint.ConstraintStatus
import io.smooth.constraint.resolution.ConstraintResolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Provider

class BatteryConstraint(
    val requiredBatteryLevel: BatteryLevelState? = BatteryLevelState.GOOD,
    val requiredBatteryChargingState: BatteryChargingState? = null
) : Constraint<BatteryConstraint> {

    private val batteryService = SmoothBatteryService.getInstance()

    override suspend fun check(): Flow<ConstraintStatus> = flow {
        batteryService.listen().collect {
            var result = true
            when (it) {
                is ChargingStateChanged -> {
                    if (requiredBatteryChargingState != null) {
                        result = requiredBatteryChargingState == it.batteryChargingState
                    }
                    if (!result) emit(ConstraintStatus.CONSTRAINT_NOT_MET)
                    if (requiredBatteryLevel != null) {
                        result = requiredBatteryLevel == batteryService.getLevelState()
                    }
                }
                is LevelChanged -> {
                    var result = true
                    if (requiredBatteryLevel != null) {
                        result = requiredBatteryLevel == it.levelState
                    }
                    if (!result) emit(ConstraintStatus.CONSTRAINT_NOT_MET)
                    if (requiredBatteryChargingState != null) {
                        result = requiredBatteryChargingState == batteryService.getChargingState()
                    }
                }
            }

            if (result) emit(ConstraintStatus.CONSTRAINT_MET)
            else emit(ConstraintStatus.CONSTRAINT_NOT_MET)
        }
    }

    override fun resolutions(): List<Provider<out ConstraintResolution<out BatteryConstraint, *>>>? =
        null


}