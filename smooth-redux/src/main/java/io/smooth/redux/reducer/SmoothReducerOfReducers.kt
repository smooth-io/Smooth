package io.smooth.redux.reducer

import io.smooth.redux.SmoothState
import org.reduxkotlin.Reducer

class SmoothReducerOfReducers(
    private val reducers: List<SmoothReducer<*>>
) : Reducer<SmoothState> {

    override fun invoke(state: SmoothState, action: Any): SmoothState {
        reducers.forEach {
            if (it.canReduce(action)) {
                return it.invoke(state, action)
            }
        }
        return state
    }

}