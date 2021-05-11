package io.smooth.constraint.work

import androidx.work.Constraints
import androidx.work.NetworkType
import io.smooth.battery.BatteryChargingState
import io.smooth.battery.BatteryLevelState
import io.smooth.constraint.Constraint
import io.smooth.network.NetworkMeteringType
import io.smooth.network.NetworkRoamingType
import io.smooth.constraint.android.BatteryConstraint
import io.smooth.constraint.android.NetworkConstraint
import io.smooth.constraint.android.StorageConstraint

object WorkConstraintsUtils {

    data class ConstraintsMappingResult(
        val workManagerConstraints: Constraints,
        val handledConstraints: List<Constraint>,
        val nonHandledConstraints: List<Constraint>
    )

    fun mapConstraints(
        constraints: List<Constraint>
    ): ConstraintsMappingResult {

        val handledConstraints = arrayListOf<Constraint>()

        val constraintsBuilder = Constraints.Builder()

        val nonHandledConstraints = constraints
            .mapNotNull {
                when (it) {
                    is BatteryConstraint -> {
                        constraintsBuilder.setRequiresCharging(
                            getRequiredChargingFromBatteryConstraint(it)
                        )

                        constraintsBuilder.setRequiresBatteryNotLow(
                            getRequiredBatteryLevelFromBatteryConstraint(it)
                        )
                        handledConstraints.add(it)
                        null
                    }

                    is NetworkConstraint -> {
                        constraintsBuilder.setRequiredNetworkType(
                            getRequiredNetworkTypeFromNetworkConstraint(
                                it
                            )
                        )
                        handledConstraints.add(it)
                        null
                    }

                    is StorageConstraint -> {
                        constraintsBuilder.setRequiresStorageNotLow(true)
                        handledConstraints.add(it)
                        null
                    }
                    else -> it
                }
            }

        return ConstraintsMappingResult(
            constraintsBuilder.build(),
            handledConstraints,
            nonHandledConstraints
        )
    }

    private fun getRequiredChargingFromBatteryConstraint(config: BatteryConstraint): Boolean =
        config.requiredBatteryChargingState == BatteryChargingState.CHARGING

    private fun getRequiredBatteryLevelFromBatteryConstraint(config: BatteryConstraint): Boolean =
        config.requiredBatteryLevel == BatteryLevelState.GOOD

    private fun getRequiredNetworkTypeFromNetworkConstraint(config: NetworkConstraint): NetworkType {

        if (config.requiredMeteringType != null) {
            val metered = config.requiredMeteringType == NetworkMeteringType.METERED
            return if (metered) NetworkType.METERED else NetworkType.UNMETERED
        }

        if (config.requiredRoamingType != null) {
            val roaming = config.requiredRoamingType == NetworkRoamingType.ROAMING
            return if (!roaming) NetworkType.NOT_ROAMING else NetworkType.CONNECTED
        }

        if (config.requiredNetworkType != null) {
            val connected =
                config.requiredNetworkType == io.smooth.network.NetworkType.CONNECTED
            return if (connected) NetworkType.CONNECTED else NetworkType.NOT_REQUIRED
        }

        return NetworkType.NOT_REQUIRED
    }

}