package io.smooth.constraint.android

import io.smooth.constraint.Constraint
import io.smooth.constraint.ConstraintStatus
import io.smooth.network.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class NetworkConstraint(
    val requiredNetworkType: NetworkType? = NetworkType.CONNECTED,
    val requiredMeteringType: NetworkMeteringType? = null,
    val requiredRoamingType: NetworkRoamingType? = null,
    private val networkService: SmoothNetworkService
) : Constraint {

    override suspend fun check(): Flow<ConstraintStatus> = flow {
        networkService.listen().collect {

            var result = true
            if (requiredNetworkType != null) {
                result = it.networkType == requiredNetworkType
            }
            if (!result) {
                emit(ConstraintStatus.CONSTRAINT_NOT_MET)
                return@collect
            }
            if (requiredMeteringType != null) {
                result = it.networkMeteringType == requiredMeteringType
            }
            if (!result) {
                emit(ConstraintStatus.CONSTRAINT_NOT_MET)
                return@collect
            }
            if (requiredRoamingType != null) {
                result = it.networkRoamingType == requiredRoamingType
            }

            if (!result) emit(ConstraintStatus.CONSTRAINT_NOT_MET)
            else emit(ConstraintStatus.CONSTRAINT_MET)
        }
    }

}