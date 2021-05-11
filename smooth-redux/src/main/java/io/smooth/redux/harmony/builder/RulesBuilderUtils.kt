package io.smooth.redux.harmony.builder

import io.smooth.redux.SmoothState
import io.smooth.redux.SmoothStateType
import io.smooth.redux.StateSection
import io.smooth.redux.harmony.ReducerModificationResult
import io.smooth.redux.harmony.ReducerRule
import io.smooth.redux.harmony.modification.Modification

class ModificationResultBuilder {

    private val activateSections = arrayListOf<Modification>()
    private val deactivateSections = arrayListOf<Modification>()

    fun activate(vararg modification: Modification) {
        activateSections.addAll(modification)
    }

    fun deactivate(vararg modification: Modification) {
        deactivateSections.addAll(modification)
    }

    fun toModificationResult(): ReducerModificationResult =
        ReducerModificationResult(activateSections, deactivateSections)

}


inline fun <reified Section : StateSection> RulesBuilder.onRule(
    noinline matching: Section.(state: SmoothState) -> Boolean,
    block: ModificationResultBuilder.() -> Unit
) {
    val modificationResultBuilder = ModificationResultBuilder()
    block(modificationResultBuilder)
    addRule(
        ReducerRule(
            matching,
            { section ->
                section is Section
            },
            modificationResultBuilder.toModificationResult()
        )
    )
}

fun RulesBuilder.onType(
    onType: SmoothStateType,
    block: ModificationResultBuilder.() -> Unit
) = onRule<StateSection>({ onType == this.type }, block)

fun RulesBuilder.onSectionName(
    onName: String,
    block: ModificationResultBuilder.() -> Unit
) = onRule<StateSection>({ onName == this.name }, block)

inline fun <reified Section : StateSection> RulesBuilder.onActivated(
    block: ModificationResultBuilder.() -> Unit
) = onRule<Section>({ isActive }, block)

inline fun <reified Section : StateSection> RulesBuilder.onDeactivated(
    block: ModificationResultBuilder.() -> Unit
) = onRule<Section>({ !isActive }, block)





