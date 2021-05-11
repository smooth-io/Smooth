package io.smooth.redux.effect

import io.smooth.redux.SmoothState
import io.smooth.redux.reducer.SmoothReducer
import org.reduxkotlin.Store
import org.reduxkotlin.middleware

fun Store<SmoothState>.effect(effect: Effect) {
    dispatch.invoke(
        NewEffectAction(effect)
    )
}