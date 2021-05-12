package io.smooth.constraint

import io.smooth.constraint.resolution.ConstraintResolution
import kotlinx.coroutines.flow.Flow
import javax.inject.Provider

interface Constraint<C : Constraint<C>> {

    suspend fun check(): Flow<ConstraintStatus>

    fun resolutions(): List<Provider<out ConstraintResolution<out C,*>>>?

}