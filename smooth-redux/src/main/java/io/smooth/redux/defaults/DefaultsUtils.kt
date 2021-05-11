package io.smooth.redux.defaults

import io.smooth.redux.SmoothState
import io.smooth.redux.SmoothStateType
import io.smooth.redux.StateSection
import io.smooth.redux.defaults.section.*
import io.smooth.redux.harmony.builder.RulesBuilder
import io.smooth.redux.harmony.builder.onActivated
import io.smooth.redux.harmony.builder.onRule
import io.smooth.redux.harmony.builder.onType
import io.smooth.redux.harmony.modification.byType
import io.smooth.redux.modify

class NoError() : Throwable()

fun defaultSections(): List<StateSection> =
    arrayListOf(
        ProcessingSection(ProcessingType.NORMAL, 0f, "", null, false),
        ErrorSection(NoError(), ErrorType.SOFT, "", null, false)
    )

fun RulesBuilder.applyDefaultRules(
    hideContentOnProcessing: Boolean = true
) {

    onRule<ErrorSection>({ isActive && errorType == ErrorType.HARD }) {
        deactivate(
            byType(SmoothStateType.Processing),
            byType(SmoothStateType.Content)
        )
    }

    onRule<ErrorSection>({ isActive && errorType == ErrorType.SOFT }) {
        deactivate(
            byType(SmoothStateType.Processing)
        )
    }

    onActivated<ProcessingSection> {
        deactivate(
            byType(SmoothStateType.Error)
        )
        if (hideContentOnProcessing) {
            deactivate(byType(SmoothStateType.Content))
        }
    }

    onActivated<ContentSection<*>> {
        deactivate(
            byType(SmoothStateType.Error),
            byType(SmoothStateType.Processing)
        )
    }

}

fun <Content> SmoothState.modifyContent(
    block: Content.() -> Content
): SmoothState = modify<ContentSection<Content>> {
    copy(
        block(this.content), isActive = true
    )
}
