package io.smooth.redux.defaults

import io.smooth.redux.SmoothState
import io.smooth.redux.defaults.section.ErrorSection
import io.smooth.redux.defaults.section.ProcessingSection
import io.smooth.redux.modify
import io.smooth.redux.reducer.SmoothReducer

class DefaultReducer(
    private val errorTypeSpecifier: ErrorTypeSpecifier
) : SmoothReducer<DefaultAction> {

    override fun canReduce(action: Any): Boolean =
        action is DefaultAction

    override fun SmoothState.reduce(action: DefaultAction): SmoothState =
        when (action) {
            is ProcessingAction -> modify<ProcessingSection> {
                copy(
                    processingType = action.processingType,
                    progress = action.progress,
                    reason = action.reason,
                    metaData = action.metaData,
                    isActive = true
                )
            }

            is ErrorAction -> modify<ErrorSection> {
                copy(
                    error = action.error,
                    reason = action.reason,
                    errorType = errorTypeSpecifier.determineErrorType(
                        this@reduce,
                        action.error,
                        action.reason
                    ),
                    recoverable = action.recoverable,
                    isActive = true
                )
            }
        }


}