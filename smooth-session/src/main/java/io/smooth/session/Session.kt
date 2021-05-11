package io.smooth.session

open class Session<SID>(
    open val id: SID,
    internal val isExpired: Boolean,
    internal val isActive: Boolean
)