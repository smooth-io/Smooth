package io.smooth.constraint.resolution


interface ConstraintResolution<Details> {

    suspend fun resolve()

    suspend fun getDetails(): Details

}