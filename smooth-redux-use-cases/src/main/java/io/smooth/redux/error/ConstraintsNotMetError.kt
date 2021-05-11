package io.smooth.redux.error

import io.smooth.constraint.Constraint

class ConstraintsNotMetError(
    private val blockingConstraints: List<Constraint>
): Throwable("Constraints not met")