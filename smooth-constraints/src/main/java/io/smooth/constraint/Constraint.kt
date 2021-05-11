package io.smooth.constraint

import kotlinx.coroutines.flow.Flow

interface Constraint {

    suspend fun check(): Flow<ConstraintStatus>

}