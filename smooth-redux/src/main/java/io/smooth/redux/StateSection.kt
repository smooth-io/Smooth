package io.smooth.redux

abstract class StateSection(
    open val isActive: Boolean,
    val name: String,
    val type: SmoothStateType,
) {
    val lastUpdatedAt: Long = System.currentTimeMillis()

    abstract fun changeActivationStatus(isActive: Boolean): StateSection
}