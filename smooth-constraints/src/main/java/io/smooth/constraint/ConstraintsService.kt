package io.smooth.constraint

import io.smooth.constraint.resolution.ConstraintResolution
import io.smooth.constraint.resolution.ConstraintResolutionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Provider
import kotlin.reflect.KClass

class ConstraintsService {

    private val resolutionService = ConstraintResolutionService()

    suspend fun check(
        vararg constraints: Constraint<*>
    ): Flow<ConstraintResult> = flow {

        val constraintsStatuses: MutableMap<Constraint<*>, ConstraintStatus> = mutableMapOf()

        constraints.forEach { constraint ->

            resolutionService.addConstraintsResolutions(constraint)

            constraint.check().collect { status ->
                constraintsStatuses[constraint] = status
                val result = checkConstraints(constraints, constraintsStatuses)
                this.emit(result)
            }

        }

    }

    suspend fun <C : Constraint<C>, CR : ConstraintResolution<C, *>> resolve(
        constraint: C,
        resolution: CR
    ) {
        resolution.resolve(constraint)
    }

    suspend fun <C : Constraint<C>, CR : ConstraintResolution<C, *>> getDetails(
        constraint: C,
        resolution: CR
    ) = resolution.getDetails(constraint)

    suspend fun addConstraintsResolutions(
        constraintClass: KClass<out Constraint<*>>,
        resolutions: List<Provider<out ConstraintResolution<out Constraint<*>, *>>>
    ) {
        resolutionService.addConstraintsResolutions(constraintClass, resolutions)
    }

    suspend fun <C : Constraint<C>> addConstraintResolution(
        constraintClass: KClass<C>,
        resolutionProvider: Provider<ConstraintResolution<C, *>>
    ) {
        resolutionService.addConstraintResolution(constraintClass, resolutionProvider)
    }


    suspend fun <C : Constraint<C>> getConstraintResolutions(constraintClass: KClass<C>): List<Provider<out ConstraintResolution<C, *>>>? =
        resolutionService.getConstraintResolutions(constraintClass)

    private fun checkConstraints(
        constraints: Array<out Constraint<*>>,
        constraintsStatuses: MutableMap<Constraint<*>, ConstraintStatus>
    ): ConstraintResult {
        if (constraintsStatuses.size != constraints.size) return ConstraintsPending(constraints)

        val blockingConstraints: MutableList<Constraint<*>> = arrayListOf()
        constraintsStatuses.forEach {
            if (it.value == ConstraintStatus.CONSTRAINT_NOT_MET) {
                blockingConstraints.add(it.key)
            }
        }

        return if (blockingConstraints.isEmpty()) ConstraintsMet(constraints)
        else ConstraintsNotMet(constraints, blockingConstraints)
    }

    companion object {

        private var instance: ConstraintsService? = null

        fun getInstance(): ConstraintsService {
            if (instance == null) {
                instance = ConstraintsService()
            }
            return instance!!
        }

    }


}