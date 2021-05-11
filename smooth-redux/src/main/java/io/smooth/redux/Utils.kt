package io.smooth.redux

import io.smooth.redux.harmony.HarmonyUtils.reactOnUpdate
import io.smooth.redux.harmony.ReducerRule
import io.smooth.redux.harmony.builder.RulesBuilder
import org.reduxkotlin.Store
import kotlin.reflect.KClass


fun <Section : StateSection> updateSectionInternally(
    smoothState: SmoothState,
    updatedSection: Section
): SmoothState = smoothState
    .copy(
        sections = smoothState.sections.plus(
            Pair(updatedSection.name, updatedSection)
        )
    ).reactOnUpdate(updatedSection)

fun <Section : StateSection> SmoothState.getSection(
    sectionClass: KClass<Section>
): Section? =
    sections.values.firstOrNull {
        it::class == sectionClass
    }?.let {
        it as Section
    }

inline fun <reified Section : StateSection> SmoothState.modify(
    noinline block: Section.() -> Section
): SmoothState = modify<Section>(Section::class, block)

fun <Section : StateSection> SmoothState.modify(
    sectionClass: KClass<Section>,
    block: Section.() -> Section
): SmoothState {
    var prevSection: StateSection? = null
    return getSection(sectionClass)?.let {
        prevSection = it
        it
    }?.let(block)?.let {
        if (it != prevSection) {
            updateSectionInternally(this, it)
        } else this
    } ?: this
}

fun <Section : StateSection> SmoothState.activate(
    sectionClass: KClass<Section>
): SmoothState = modify(sectionClass) {
    if (isActive) this
    else changeActivationStatus(true) as Section
}

inline fun <reified Section : StateSection> SmoothState.activate():
        SmoothState = activate<Section>(Section::class)

fun <Section : StateSection> SmoothState.deActivate(
    sectionClass: KClass<Section>
): SmoothState = modify(sectionClass) {
    if (!isActive) this
    else changeActivationStatus(false) as Section
}

inline fun <reified Section : StateSection> SmoothState.deActivate():
        SmoothState = deActivate<Section>(Section::class)

fun createState(
    sections: List<StateSection>,
    rulesBuilderBlock: RulesBuilder.() -> Unit = {}
): SmoothState {
    val rulesBuilder = RulesBuilder()
    rulesBuilderBlock.invoke(rulesBuilder)
    return SmoothState(
        sections = sections.map {
            it.name to it
        }.toMap(),
        arrayListOf(),
        rulesBuilder.getRules()
    )
}

fun createState(
    sections: List<StateSection>,
    rules: List<ReducerRule<*>>
): SmoothState {
    return SmoothState(
        sections = sections.map {
            it.name to it
        }.toMap(),
        arrayListOf(),
        rules
    )
}

fun Store<SmoothState>.dispatch(
    action: Any,
    timestamp: Long
) {
    if (timestamp >= getState.invoke().lastUpdatedAt) dispatch(action)
}

suspend fun Store<SmoothState>.runSafe(
    block: suspend SafeRunner.() -> Unit
) {
    val safeRunner = SafeRunner(this)
    block(safeRunner)
}

open class SafeRunner(
    val store: Store<SmoothState>
) {

    private val startTimeStamp = System.currentTimeMillis()

    fun dispatch(action: Any) {
        store.dispatch(action, startTimeStamp)
    }

    fun dispatchNotSafe(action: Any) {
        store.dispatch(action)
    }

    fun getState(): SmoothState =
        store.getState.invoke()

}