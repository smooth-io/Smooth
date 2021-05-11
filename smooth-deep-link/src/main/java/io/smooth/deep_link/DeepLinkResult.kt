package io.smooth.deep_link

import io.smooth.constraint.ConstraintResult
import io.smooth.deep_link.target.DeepLinkTarget

class DeepLinkResult<T : DeepLinkTarget>(
    val target: T,
    val constraintsResult: ConstraintResult
)