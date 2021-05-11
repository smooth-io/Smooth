package io.smooth.redux

import io.smooth.redux.effect.Effect
import io.smooth.redux.harmony.ReducerRule

data class SmoothState(
    val sections: Map<String, StateSection>,
    val effects: List<Effect> = arrayListOf(),
    val rules: List<ReducerRule<*>>? = null,
    val lastUpdatedAt: Long = System.currentTimeMillis()
)