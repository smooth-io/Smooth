package io.smooth.redux.harmony

import io.smooth.redux.SmoothState
import io.smooth.redux.StateSection

class ReducerRule<Section : StateSection>(
    val matching: Section.(state: SmoothState) -> Boolean,
    val canApplyModification: (updatedSection: StateSection) -> Boolean,
    val modification: ReducerModificationResult
) {

    fun getModifications(
        state: SmoothState,
        updatedSection: StateSection
    ): ReducerModificationResult? =
        if (matching.invoke(updatedSection as Section, state)) modification
        else null

}