package io.smooth.constraint

import io.smooth.constraint.resolution.ConstraintResolution


suspend fun <C : Constraint<C>> Pair<C, ConstraintResolution<C, *>>.getDetails() =
    second.getDetails(first)

suspend fun <C : Constraint<C>> Pair<C, ConstraintResolution<C, *>>.resolve() =
    second.resolve(first)

data class ConstraintResolutionDetails(
    val constraint: Constraint<*>,
    val resolution: ConstraintResolution<Constraint<*>, *>,
    val details: Any
){

    suspend fun resolve(){
        resolution.resolve(constraint)
    }

}

suspend fun Pair<Constraint<*>, List<ConstraintResolution<Constraint<*>, *>>>.resolutionDetails() =
    second.mapNotNull {
        val details = it.getDetails(first)
        if (details != null) ConstraintResolutionDetails(first, it, details)
        else null
    }

suspend fun ConstraintsNotMet.resolutionDetails() =
    getResolutions().map {
        it.resolutionDetails()
    }.flatten()

suspend fun ConstraintsNotMet.getResolutions(): List<Pair<Constraint<*>, List<ConstraintResolution<Constraint<*>, *>>>> {
    val service = ConstraintsService.getInstance()

    return blockingConstraints.mapNotNull {
        it to (service.getConstraintResolutions(it::class)?.map {
            it.get() as ConstraintResolution<Constraint<*>, *>
        } ?: arrayListOf())
    }
}


