package io.smooth.redux.defaults.section

import io.smooth.redux.SmoothStateType
import io.smooth.redux.StateSection

data class ProcessingSection(
    val processingType: ProcessingType,
    val progress: Float = 0f,
    val reason: Any,
    val metaData: Map<String, *>? = null,
    override val isActive: Boolean = false
) : StateSection(isActive, "__processing_section", SmoothStateType.Processing) {

    override fun changeActivationStatus(isActive: Boolean): StateSection =
        copy(isActive = isActive)

}