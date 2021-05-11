package io.smooth.redux.effect

import io.smooth.redux.SmoothState
import io.smooth.redux.reducer.SmoothReducer

class EffectsReducer : SmoothReducer<NewEffectAction> {

    override fun canReduce(action: Any): Boolean =
        action is NewEffectAction

    override fun SmoothState.reduce(action: NewEffectAction): SmoothState =
        copy(
            effects = this.effects.plus(action.effect.apply {
                consumed = false
            })
        )


}