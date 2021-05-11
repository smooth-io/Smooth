package io.smooth.redux.harmony

import io.smooth.redux.SmoothState
import io.smooth.redux.StateSection
import io.smooth.redux.activate
import io.smooth.redux.deActivate

object HarmonyUtils {

    fun SmoothState.reactOnUpdate(updatedSection: StateSection): SmoothState {
        var updatedState = this
        this.rules?.forEach {
            if (it.canApplyModification.invoke(updatedSection)) {
                it.getModifications(this, updatedSection)?.also {

                    it.activateSections?.forEach {
                        it.getSectionsToModify(this).forEach {
                            updatedState = updatedState.activate(it::class)
                        }
                    }

                    it.deactivateSections?.forEach {
                        it.getSectionsToModify(this).forEach {
                            updatedState = updatedState.deActivate(it::class)
                        }
                    }

                }
            }
        }

        return updatedState
    }

}