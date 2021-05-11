package io.smooth.redux.harmony

import io.smooth.redux.harmony.modification.Modification

data class ReducerModificationResult(
    val activateSections: List<Modification>?,
    val deactivateSections: List<Modification>?
)
