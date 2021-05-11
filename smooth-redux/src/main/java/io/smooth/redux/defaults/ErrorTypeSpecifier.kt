package io.smooth.redux.defaults

import io.smooth.redux.SmoothState
import io.smooth.redux.defaults.section.ErrorType

interface ErrorTypeSpecifier {

    fun determineErrorType(
        state: SmoothState,
        error: Throwable,
        reason: Any
    ): ErrorType

}