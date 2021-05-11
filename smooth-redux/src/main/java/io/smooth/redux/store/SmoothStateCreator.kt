package io.smooth.redux.store

import io.smooth.redux.StateSection
import io.smooth.redux.harmony.builder.RulesBuilder

interface SmoothStateCreator {

    fun sections(): List<StateSection>

    fun rulesBuilderBlock(): RulesBuilder.() -> Unit

}