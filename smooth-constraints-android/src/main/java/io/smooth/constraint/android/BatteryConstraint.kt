package io.smooth.constraint.android

import io.smooth.battery.*
import io.smooth.constraint.Constraint
import io.smooth.constraint.ConstraintStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class BatteryConstraint(
    val requiredBatteryLevel: BatteryLevelState? = BatteryLevelState.GOOD,
    val requiredBatteryChargingState: BatteryChargingState? = null,
    private val batteryService: SmoothBatteryService
) : Constraint {

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

}