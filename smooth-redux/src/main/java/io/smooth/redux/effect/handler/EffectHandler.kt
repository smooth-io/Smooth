package io.smooth.redux.effect.handler

import io.smooth.redux.effect.Effect

abstract class EffectHandler<E : Effect> {

    suspend fun handle(effect: E) {
        effect.consumed = true
        val result = handleEffect(effect)
        if (result) effect.consumed = true
    }

    protected abstract suspend fun handleEffect(effect: E): Boolean

}