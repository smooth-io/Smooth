package io.smooth.constraint.android

import android.content.Context
import io.smooth.constraint.Constraint
import io.smooth.constraint.ConstraintStatus
import io.smooth.constraint.resolution.ConstraintResolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Provider

class StorageConstraint(
    private val context: Context,
    private val lowStoragePerc: Float = 10f
) : Constraint<StorageConstraint> {

    override suspend fun check(): Flow<ConstraintStatus> = flow {
        val result = context.cacheDir.freeSpace >= lowStoragePerc

        if (result) emit(ConstraintStatus.CONSTRAINT_MET)
        else emit(ConstraintStatus.CONSTRAINT_NOT_MET)
    }

    override fun resolutions(): List<Provider<out ConstraintResolution<out StorageConstraint, *>>>? =
        null

}