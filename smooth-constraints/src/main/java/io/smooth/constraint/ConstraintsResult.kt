package io.smooth.constraint

sealed class ConstraintResult(
    open val constraints: Array<out Constraint<*>>
)

data class ConstraintsPending(override val constraints: Array<out Constraint<*>>) :
    ConstraintResult(constraints) {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}

data class ConstraintsMet(override val constraints: Array<out Constraint<*>>) :
    ConstraintResult(constraints) {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}

data class ConstraintsNotMet(
    override val constraints: Array<out Constraint<*>>,
    val blockingConstraints: List<Constraint<*>>
) : ConstraintResult(constraints) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConstraintsNotMet) return false

        if (!constraints.contentEquals(other.constraints)) return false
        if (blockingConstraints != other.blockingConstraints) return false

        return true
    }

    override fun hashCode(): Int {
        var result = constraints.contentHashCode()
        result = 31 * result + blockingConstraints.hashCode()
        return result
    }
}
