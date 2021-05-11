package io.smooth.redux.reducer

import io.smooth.redux.SmoothState
import org.reduxkotlin.Reducer

interface SmoothReducer<Action> : Reducer<SmoothState> {

    override fun invoke(state: SmoothState, action: Any): SmoothState =
        (action as? Action)?.let {
            state.reduce(it)
        } ?: state

    fun canReduce(action: Any): Boolean

    fun SmoothState.reduce(action: Action): SmoothState

}