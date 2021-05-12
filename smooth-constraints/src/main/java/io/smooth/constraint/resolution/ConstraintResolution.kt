package io.smooth.constraint.resolution

import io.smooth.constraint.Constraint


interface ConstraintResolution<C : Constraint<*>, Details> {

    suspend fun resolve(constraint: Constraint<*>)

    suspend fun getDetails(constraint: Constraint<*>): Details

}