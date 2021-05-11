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
        vararg constraints: Constraint
    ): Flow<ConstraintResult> = flow {

        val constraintsStatuses: MutableMap<Constraint, ConstraintStatus> = mutableMapOf()

        constraints.forEach { constraint ->

            constraint.check().collect { status ->
                constraintsStatuses[constraint] = status
                val result = checkConstraints(constraints, constraintsStatuses)
                this.emit(result)
            }

        }

    }

    suspend fun addConstraintResolution(
        constraintClass: KClass<Constraint>,
        resolutionProvider: Provider<ConstraintResolution<*>>
    ) {
        resolutionService.addConstraintResolution(constraintClass, resolutionProvider)
    }

    suspend fun getConstraintResolutions(constraintClass: KClass<Constraint>): List<Provider<ConstraintResolution<*>>>? =
        resolutionService.getConstraintResolutions(constraintClass)

    private fun checkConstraints(
        constraints: Array<out Constraint>,
        constraintsStatuses: MutableMap<Constraint, ConstraintStatus>
    ): ConstraintResult {
        if (constraintsStatuses.size != constraints.size) return ConstraintsPending()

        val blockingConstraints: MutableList<Constraint> = arrayListOf()
        constraintsStatuses.forEach {
            if (it.value == ConstraintStatus.CONSTRAINT_NOT_MET) {
                blockingConstraints.add(it.key)
            }
        }

        return if (blockingConstraints.isEmpty()) ConstraintsMet()
        else ConstraintsNotMet(blockingConstraints)
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