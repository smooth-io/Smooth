package io.smooth.redux.defaults

import io.smooth.redux.defaults.section.ProcessingType

sealed class DefaultAction
class ProcessingAction(
    val processingType: ProcessingType,
    val progress: Float = 0f,
    val reason: Any,
    val metaData: Map<String, *>? = null
) : DefaultAction()

class ErrorAction(
    val error: Throwable,
    val reason: Any,
    val recoverable: Recoverable? = null
) : DefaultAction()