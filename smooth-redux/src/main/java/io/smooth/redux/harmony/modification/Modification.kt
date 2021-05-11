package io.smooth.redux.harmony.modification

import io.smooth.redux.SmoothState
import io.smooth.redux.StateSection

class Modification(
    private val matching: StateSection.() -> Boolean
) {

    fun getSectionsToModify(state: SmoothState): List<StateSection> =
        state.sections.values.filter { isMatching(it) }

    private fun isMatching(
        section: StateSection
    ): Boolean = matching.invoke(section)

}