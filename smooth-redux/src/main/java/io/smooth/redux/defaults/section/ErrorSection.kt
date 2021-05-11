package io.smooth.redux.defaults.section

import io.smooth.redux.SmoothStateType
import io.smooth.redux.StateSection
import io.smooth.redux.defaults.Recoverable


data class ErrorSection(
    val error: Throwable,
    val errorType: ErrorType,
    val reason: Any,
    val recoverable: Recoverable? = null,
    override val isActive: Boolean = false
) : StateSection(isActive, "__error_section", SmoothStateType.Error) {

    override fun changeActivationStatus(isActive: Boolean): StateSection =
        copy(isActive = isActive)

}