package io.smooth.deep_link.target

import io.smooth.constraint.Constraint

open class DeepLinkTarget(
    open val requiredConstraints: List<Constraint>
)