package io.smooth.constraint

sealed class ConstraintResult
class ConstraintsPending: ConstraintResult() {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}

class ConstraintsMet : ConstraintResult() {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}

data class ConstraintsNotMet(
    val blockingConstraints: List<Constraint>
) : ConstraintResult()
