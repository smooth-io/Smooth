package io.smooth.use_cases

import io.smooth.constraint.Constraint

data class ConstraintsHandler(
    val handledConstraints: List<Constraint<*>>?,
    val nonHandledConstraints: List<Constraint<*>>?
)